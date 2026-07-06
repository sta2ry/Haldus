package org.featx.sta2ry.haldus.endpoint

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import org.featx.spec.enums.BusinessError
import org.featx.spec.model.BaseResponse
import org.featx.sta2ry.haldus.handler.AuthHandler
import org.featx.sta2ry.haldus.model.*
import org.featx.sta2ry.haldus.service.AuthService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthEndpoint : AuthHandler {
    @Inject
    private lateinit var authService: AuthService

    override fun register(ctx: RoutingContext) {
        val request = ctx.body().asJsonObject().mapTo(RegisterRequest::class.java)
        authService.register(request).onComplete { ar ->
            if (ar.succeeded()) {
                ctx.response()
                    .setStatusCode(201)
                    .putHeader("Content-Type", "application/json")
                    .end(Buffer.buffer(Json.encodePrettily(ar.result())))
            } else {
                val message = ar.cause().message ?: "register failed"
                val statusCode = if (message.contains("already exists")) 409 else 400
                val response = ctx.response()
                    .setStatusCode(statusCode)
                    .putHeader("Content-Type", "application/json")
                response.end(Buffer.buffer(Json.encodePrettily(BaseResponse.occur<Nothing>(BusinessError.SERVER_ERROR))))
            }
        }
    }

    override fun login(ctx: RoutingContext) {
        val request = ctx.body().asJsonObject().mapTo(LoginRequest::class.java)
        authService.login(request).onComplete { ar ->
            if (ar.succeeded()) {
                ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Buffer.buffer(Json.encodePrettily(ar.result())))
            } else {
                val response = ctx.response()
                    .setStatusCode(401)
                    .putHeader("Content-Type", "application/json")
                response.end(Buffer.buffer(Json.encodePrettily(BaseResponse.occur<Nothing>(BusinessError.SERVER_ERROR))))
            }
        }
    }

    override fun refresh(ctx: RoutingContext) {
        val request = ctx.body().asJsonObject().mapTo(RefreshRequest::class.java)
        authService.refreshToken(request).onComplete { ar ->
            if (ar.succeeded()) {
                ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Buffer.buffer(Json.encodePrettily(ar.result())))
            } else {
                val response = ctx.response()
                    .setStatusCode(401)
                    .putHeader("Content-Type", "application/json")
                response.end(Buffer.buffer(Json.encodePrettily(BaseResponse.occur<Nothing>(BusinessError.SERVER_ERROR))))
            }
        }
    }

    override fun oauthRedirect(ctx: RoutingContext) {
        val provider = ctx.pathParam("provider")
        authService.getOAuthRedirectUrl(provider).onComplete { ar ->
            if (ar.succeeded()) {
                ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Buffer.buffer(Json.encodePrettily(mapOf("redirect_url" to ar.result()))))
            } else {
                val response = ctx.response()
                    .setStatusCode(400)
                    .putHeader("Content-Type", "application/json")
                response.end(Buffer.buffer(Json.encodePrettily(BaseResponse.occur<Nothing>(BusinessError.SERVER_ERROR))))
            }
        }
    }

    override fun oauthCallback(ctx: RoutingContext) {
        val provider = ctx.pathParam("provider")
        val code = ctx.request().getParam("code") ?: ""
        val state = ctx.request().getParam("state") ?: ""

        val request = OAuthCallbackRequest(
            provider = provider,
            code = code,
            state = state,
        )
        authService.oauthCallback(request).onComplete { ar ->
            if (ar.succeeded()) {
                ctx.response()
                    .putHeader("Content-Type", "application/json")
                    .end(Buffer.buffer(Json.encodePrettily(ar.result())))
            } else {
                val response = ctx.response()
                    .setStatusCode(401)
                    .putHeader("Content-Type", "application/json")
                response.end(Buffer.buffer(Json.encodePrettily(BaseResponse.occur<Nothing>(BusinessError.SERVER_ERROR))))
            }
        }
    }
}
