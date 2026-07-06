package org.featx.sta2ry.haldus

import com.google.inject.AbstractModule
import com.google.inject.Provides
import io.vertx.core.Vertx
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

import org.featx.spec.feature.IdGenerate
import org.featx.spec.feature.SnowflakeIdWorker
import org.featx.sta2ry.haldus.endpoint.AuthEndpoint
import org.featx.sta2ry.haldus.endpoint.UserInfoEndpoint
import org.featx.sta2ry.haldus.handler.AuthHandler
import org.featx.sta2ry.haldus.handler.UserInfoHandler
import org.featx.sta2ry.haldus.repo.UserInfoRepo
import org.featx.sta2ry.haldus.repo.UserInfoRepository
import org.featx.sta2ry.haldus.service.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Singleton


class AppContext(var vertx: Vertx) : AbstractModule() {

  override fun configure() {
    bind(Vertx::class.java).toInstance(vertx)

    bind(UserInfoRepo::class.java).to(UserInfoRepository::class.java).`in`(Singleton::class.java)
    bind(UserInfoService::class.java).to(UserInfoServiceImpl::class.java).`in`(Singleton::class.java)
    bind(UserInfoHandler::class.java).to(UserInfoEndpoint::class.java).`in`(Singleton::class.java)

    bind(AuthService::class.java).to(AuthServiceImpl::class.java).`in`(Singleton::class.java)
    bind(PasswordService::class.java).to(PasswordServiceImpl::class.java).`in`(Singleton::class.java)
    bind(TokenService::class.java).to(TokenServiceImpl::class.java).`in`(Singleton::class.java)
    bind(OAuthService::class.java).to(OAuthServiceImpl::class.java).`in`(Singleton::class.java)
    bind(AuthHandler::class.java).to(AuthEndpoint::class.java).`in`(Singleton::class.java)
  }

  @Provides
  @Singleton
  fun jwtAuthProvider(): JWTAuth {
    return JWTAuth.create(
      vertx,
      JWTAuthOptions()
        .setJWTOptions(
          JWTOptions()
            .setIssuer("haldus")
            .setExpiresInSeconds(900)
        )
    )
  }

  @Provides
  @Singleton
  fun router(
    userInfoHandler: UserInfoHandler,
    authHandler: AuthHandler
  ): Router {
    val router = Router.router(vertx)

    router.post("/auth/register")
      .handler(BodyHandler.create())
      .handler { ctx -> authHandler.register(ctx) }

    router.post("/auth/login")
      .handler(BodyHandler.create())
      .handler { ctx -> authHandler.login(ctx) }

    router.post("/auth/refresh")
      .handler(BodyHandler.create())
      .handler { ctx -> authHandler.refresh(ctx) }

    router.get("/auth/oauth/:provider")
      .handler { ctx -> authHandler.oauthRedirect(ctx) }

    router.get("/auth/oauth/:provider/callback")
      .handler { ctx -> authHandler.oauthCallback(ctx) }

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
