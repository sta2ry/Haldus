package org.featx.sta2ry.haldus.handler

import io.vertx.core.buffer.Buffer
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import org.featx.spec.model.PageResponse
import org.featx.sta2ry.haldus.model.*

interface UserInfoHandler : Handler<RoutingContext> {
    fun postUserInfo(accountSave: AccountSave): Future<AccountShow>
    fun getUserInfo(userCode: String): Future<AccountDetail>
    fun deleteUserInfo(userCode: String): Future<Boolean>
    fun page(accountPageRequest: AccountPageRequest): Future<PageResponse<AccountItem>>

    override fun handle(ctx: RoutingContext) {
        when (ctx.request().method()) {
            HttpMethod.POST -> {
                val body = ctx.body().asJsonObject().mapTo(AccountSave::class.java)
                postUserInfo(body).onSuccess { result ->
                    ctx.response().setStatusCode(201)
                        .putHeader("Content-Type", "application/json")
                        .end(Buffer.buffer(Json.encodePrettily(result)))
                }.onFailure { err ->
                    ctx.response().setStatusCode(400)
                        .putHeader("Content-Type", "application/json")
                        .end(Buffer.buffer("""{"error":"${err.message}"}"""))
                }
            }
            HttpMethod.GET -> {
                val userCode = ctx.pathParam("userCode")
                if (userCode != null) {
                    getUserInfo(userCode).onSuccess { result ->
                        ctx.response().putHeader("Content-Type", "application/json")
                            .end(Buffer.buffer(Json.encodePrettily(result)))
                    }.onFailure { err ->
                        ctx.response().setStatusCode(400)
                            .putHeader("Content-Type", "application/json")
                            .end(Buffer.buffer("""{"error":"${err.message}"}"""))
                    }
                } else {
                    val pageNum = ctx.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
                    val size = ctx.queryParam("size").firstOrNull()?.toIntOrNull() ?: 10
                    val request = AccountPageRequest()
                    request.page = pageNum
                    request.size = size
                    page(request).onSuccess { result ->
                        ctx.response().putHeader("Content-Type", "application/json")
                            .end(Buffer.buffer(Json.encodePrettily(result)))
                    }.onFailure { err ->
                        ctx.response().setStatusCode(400)
                            .putHeader("Content-Type", "application/json")
                            .end(Buffer.buffer("""{"error":"${err.message}"}"""))
                    }
                }
            }
            HttpMethod.DELETE -> {
                val userCode = ctx.pathParam("userCode")
                deleteUserInfo(userCode).onSuccess { result ->
                    ctx.response().putHeader("Content-Type", "application/json")
                        .end(Buffer.buffer(Json.encodePrettily(result)))
                }.onFailure { err ->
                    ctx.response().setStatusCode(400)
                        .putHeader("Content-Type", "application/json")
                        .end(Buffer.buffer("""{"error":"${err.message}"}"""))
                }
            }
            else -> {
                ctx.response().setStatusCode(405).end()
            }
        }
    }
}
