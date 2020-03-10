package org.featx.sta2ry.haldus.handler.model;

import org.featx.sta2ry.haldus.context.HaldusException
import java.io.Serializable

data class Error(
    var code: String? = null,
    var message: String? = null,
    var tips: String? = null
) : Serializable {
    companion object {
        fun from(throwable: Throwable): Error {
            if (throwable is HaldusException) {
                return Error(throwable.code, throwable.message, throwable.tips)
            }
            return Error("", throwable.message, "")
        }
    }
}
