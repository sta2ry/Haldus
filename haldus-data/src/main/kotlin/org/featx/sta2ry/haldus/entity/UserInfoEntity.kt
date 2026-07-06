package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractUnified

class UserInfoEntity(
    var username: String? = null,
    var password: String? = null,
    var enable: Boolean? = null,
    var avatar: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var emailVerified: Boolean? = null,
    var phoneVerified: Boolean? = null,
) : AbstractUnified<Long>()
