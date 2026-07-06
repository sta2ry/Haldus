package org.featx.sta2ry.haldus.service

import io.vertx.core.Future

data class OAuthUserInfo(
    val providerUserId: String,
    val displayName: String?,
    val email: String?,
    val avatarUrl: String?,
)

interface OAuthService {
    fun getRedirectUrl(provider: String): String
    fun exchangeCode(provider: String, code: String): Future<OAuthUserInfo>
}
