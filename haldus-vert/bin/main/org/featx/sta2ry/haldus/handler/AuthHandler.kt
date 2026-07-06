package org.featx.sta2ry.haldus.handler

import io.vertx.ext.web.RoutingContext

interface AuthHandler {
    fun register(ctx: RoutingContext)
    fun login(ctx: RoutingContext)
    fun refresh(ctx: RoutingContext)
    fun oauthRedirect(ctx: RoutingContext)
    fun oauthCallback(ctx: RoutingContext)
}
