package org.featx.sta2ry.haldus.transmit.domain;

import java.io.Serializable
import java.time.LocalDateTime

data class UserData(
    var id: Long? = null,
    var code: String? = null,
    var name: String? = null,
    var type: Int? = null,
    var username: String? = null,
    var password: String? = null,
    var enable: Boolean? = null,
    var avatar: String? = null,
    var email: String? = null,
    var emailVerified: Boolean? = null,
    var phone: String? = null,
    var phoneVerified: Boolean? = null,
    var isDeleted: Boolean = false,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) : Serializable {
    constructor() : this(0)
}
