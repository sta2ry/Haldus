package org.featx.sta2ry.haldus.handler.model.user;

import java.io.Serializable
import java.time.LocalDateTime

data class SessionCreation(
    var key: Int? = null,
    var value: String? = null,
    var type: Int? = null
) : Serializable

data class SessionVerify(
    var key: Int? = null,
    var value: String? = null,
    var code: String? = null
) : Serializable

data class SessionInfo(
    var token: String? = null,
    var key: Int? = null,
    var value: String? = null,
    var type: Int? = null,
    var start: LocalDateTime? = null,
    var duration: Long? = null
) : Serializable

data class SessionUpdate(
    var token: String? = null
) : Serializable

data class SessionDelete(
    var token: String? = null
) : Serializable
