package org.featx.sta2ry.haldus.entity

import java.io.Serializable

data class UserEntity(
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
    var phoneVerified: Boolean? = null
) : Serializable {
    constructor() : this(0)
}
