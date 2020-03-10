package org.featx.sta2ry.haldus.handler

import io.reactivex.functions.Consumer
import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import org.featx.sta2ry.haldus.handler.model.Error
import org.featx.sta2ry.haldus.handler.model.user.UserInfo

abstract class BaseHandler : Handler<RoutingContext> {

    fun onSuccess(rc: RoutingContext?): Consumer<Any>? {
        return Consumer {
            rc?.response()
                ?.putHeader("content-type", "application/json")
                ?.end("{\"success\": true, \"result\": " + Json.encode(it) + "}")
        }
    }

    fun onError(rc: RoutingContext?): Consumer<in Throwable>? {
        return Consumer {
            rc?.response()
                ?.putHeader("content-type", "application/json")
                ?.end("{\"success\": false, \"result\": " + Json.encode(toBusinessErrorModel(it)) + "}")
        }
    }

    private fun toBusinessErrorModel(throwable: Throwable): Error {
        return Error.from(throwable)
    }

    override fun handle(rc: RoutingContext?) {
        when (rc?.request()?.method()) {
            HttpMethod.GET -> configGetPaths(rc)
            HttpMethod.POST -> configPostPaths(rc)
//            PUT -> putBranchAsPath(rc)
//            HEAD -> headBranchAsPath(rc)
//            OPTIONS -> optionsBranchAsPath(rc)
//            DELETE -> deleteBranchAsPath(rc)
            else -> rc?.response()?.end()
        }
    }

    open fun configGetPaths(rc: RoutingContext?) {
        throw Exception("")
    }

    open fun configPostPaths(rc: RoutingContext?) {
        throw Exception("")
    }
}