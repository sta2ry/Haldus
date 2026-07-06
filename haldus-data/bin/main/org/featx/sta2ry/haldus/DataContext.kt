package org.featx.sta2ry.haldus

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.vertx.core.Vertx
import io.vertx.mysqlclient.MySQLBuilder
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import org.featx.sta2ry.haldus.repo.AccountRepo
import org.featx.sta2ry.haldus.repo.AccountRepository
import org.featx.sta2ry.haldus.repo.AuthRepo
import org.featx.sta2ry.haldus.repo.AuthRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

class DataContext(var vertx: Vertx) : AbstractModule() {

    override fun configure() {
        bind(AccountRepo::class.java).to(AccountRepository::class.java).`in`(Singleton::class.java)
        bind(AuthRepo::class.java).to(AuthRepository::class.java).`in`(Singleton::class.java)
    }

    @Provides
    @Singleton
    fun provideClient(): MySQLConnectOptions {
        return MySQLConnectOptions()
            .setHost("localhost")
            .setPort(3308)
            .setDatabase("account")
            .setUser("ex7ept")
            .setPassword("ex7ept")
            .setCachePreparedStatements(true)
            .setPreparedStatementCacheMaxSize(250)
            .setPreparedStatementCacheSqlLimit(2048)
    }

    @Provides
    @Singleton
    fun providePool(sqlConnect: MySQLConnectOptions): Pool {
        return MySQLBuilder.pool()
            .connectingTo(sqlConnect)
            .with(
                PoolOptions()
                    .setMaxSize(20)
                    .setIdleTimeout(10).setIdleTimeoutUnit(TimeUnit.SECONDS)
                    .setConnectionTimeout(10).setConnectionTimeoutUnit(TimeUnit.SECONDS)
            )
            .using(vertx)
            .build()
    }
}