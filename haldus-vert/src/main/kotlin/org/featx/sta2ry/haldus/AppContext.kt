package org.featx.sta2ry.haldus

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

import org.featx.spec.feature.IdGenerate
import org.featx.spec.feature.SnowflakeIdWorker
import org.featx.sta2ry.haldus.endpoint.UserInfoEndpoint
import org.featx.sta2ry.haldus.handler.UserInfoHandler
import org.featx.sta2ry.haldus.service.UserInfoService
import org.featx.sta2ry.haldus.service.UserInfoServiceImpl
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Singleton


class AppContext(var vertx: Vertx) : AbstractModule() {

  override fun configure() {
    bind(UserInfoService::class.java).to(UserInfoServiceImpl::class.java).`in`(Singleton::class.java)
    bind(UserInfoHandler::class.java).to(UserInfoEndpoint::class.java).`in`(Singleton::class.java)
  }

  @Provides
  @Singleton
  fun router(userInfoHandler: UserInfoHandler): Router {
    val router = Router.router(vertx)
//    handlers.forEach {
//      it.
//    }
    router.route().path("/user/*").handler(userInfoHandler)
    return router
  }

  @Provides
  @Singleton
  fun idGenerator(): IdGenerate {
    val epoch = LocalDateTime.parse("2021-07-01T00:00:00").toEpochSecond(ZoneOffset.UTC)
    return SnowflakeIdWorker(epoch, 1, 1)
  }
}
