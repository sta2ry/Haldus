package org.featx.sta2ry.haldus.service

import org.featx.sta2ry.haldus.model.TokenResponse
import org.featx.sta2ry.haldus.model.UserBrief

interface TokenService {
    fun generateTokens(user: UserBrief): TokenResponse
    fun verifyRefreshToken(token: String): UserBrief?
    fun verifyAccessToken(token: String): UserBrief?
}
