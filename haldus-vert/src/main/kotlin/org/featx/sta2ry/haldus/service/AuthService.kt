package org.featx.sta2ry.haldus.service

import io.vertx.core.Future
import org.featx.sta2ry.haldus.model.*

interface AuthService {
    fun register(request: RegisterRequest): Future<TokenResponse>
    fun login(request: LoginRequest): Future<TokenResponse>
    fun refreshToken(request: RefreshRequest): Future<TokenResponse>
    fun oauthCallback(request: OAuthCallbackRequest): Future<TokenResponse>
    fun getOAuthRedirectUrl(provider: String): Future<String>
}
