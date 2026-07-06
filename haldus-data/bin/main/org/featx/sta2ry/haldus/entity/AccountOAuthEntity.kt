package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractUnified
import java.time.LocalDateTime

class AccountOAuthEntity(
    var userId: Long? = null,
    var provider: String = "",
    var providerUserId: String = "",
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var tokenExpiresAt: LocalDateTime? = null,
    var providerData: String? = null,
    var status: Int? = null,
) : AbstractUnified<Long>()
