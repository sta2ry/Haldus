package org.featx.sta2ry.haldus

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.jdbc.JDBCClient
import io.vertx.reactivex.ext.sql.SQLClient
import io.vertx.reactivex.redis.client.Redis
import io.vertx.redis.client.RedisOptions
import org.featx.sta2ry.haldus.service.SessionService
import org.featx.sta2ry.haldus.service.SessionServiceImpl
import org.featx.sta2ry.haldus.service.UserService
import org.featx.sta2ry.haldus.service.UserServiceImpl
import org.featx.sta2ry.haldus.transmit.repository.UserRepo
import org.featx.sta2ry.haldus.transmit.repository.UserRepository
import javax.inject.Singleton


class AppContext : AbstractModule() {

    override fun configure() {
        bind(UserService::class.java).to(UserServiceImpl::class.java).`in`(Singleton::class.java)
        bind(UserRepo::class.java).to(UserRepository::class.java).`in`(Singleton::class.java)
        bind(SessionService::class.java).to(SessionServiceImpl::class.java).`in`(Singleton::class.java)
        this.config = Vertx.currentContext().config()
    }

    lateinit var config: JsonObject

    @Provides
    @Singleton
    fun provideJDBCClient(vertx: Vertx): SQLClient {
        return JDBCClient(
            io.vertx.ext.jdbc.JDBCClient.createShared(
                vertx, JsonObject()
                    .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
                    .put("driverClassName", "com.mysql.cj.jdbc.Driver")
                    .put("jdbcUrl", config.getString("jdbc.url", "jdbc:mysql://localhost:3306/haldus"))
                    .put("username", config.getString("jdbc.username", "root"))
                    .put("password", config.getString("jdbc.password", "root"))
                    .put("maximumPoolSize", config.getInteger("datasource.pool.max.size", 20))
                    .put("minimumIdle", config.getInteger("datasource.pool.min.idle", 20))
                    .put("cachePrepStmts", config.getBoolean("datasource.cache.prep.stmts", true))
                    .put("prepStmtCacheSize", config.getInteger("datasource.prep.stmt.cache.size", 250))
                    .put("prepStmtCacheSqlLimit", config.getInteger("datasource.prep.stmt.cache.sql.limit", 2048))
            )
        )
    }

    @Provides
    @Singleton
    fun provideRedis(vertx: Vertx): Redis {
        return Redis.createClient(
            io.vertx.reactivex.core.Vertx(vertx), RedisOptions(
                JsonObject()
                    .put("host", config.getString("redis.host", "localhost"))
                    .put("port", config.getInteger("redis.port", 6379))
                    .put("select", config.getInteger("redis.db", 0))
                    .put("auth", config.getString("redis.password", "root"))
            )
        )
    }
}
