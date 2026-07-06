package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractUnified

class AccountAuthEntity(
    var email: String? = null,
    var phone: String? = null,
    var passwordHash: String? = null,
    var displayName: String? = null,
    var avatarUrl: String? = null,
    var emailVerified: Boolean = false,
    var phoneVerified: Boolean = false,
    var lastLoginAt: java.time.LocalDateTime? = null,
    var status: Int? = null,
) : AbstractUnified<Long>()
