package org.featx.sta2ry.haldus.model

data class RegisterRequest(
    val email: String? = null,
    val phone: String? = null,
    val password: String,
    val displayName: String? = null,
)

data class LoginRequest(
    val account: String,
    val password: String,
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val user: UserBrief,
)

data class UserBrief(
    val id: Long,
    val code: String,
    val displayName: String?,
    val email: String?,
    val phone: String?,
    val avatarUrl: String?,
)

data class RefreshRequest(
    val refreshToken: String,
)

data class OAuthCallbackRequest(
    val provider: String,
    val code: String,
    val state: String,
)
