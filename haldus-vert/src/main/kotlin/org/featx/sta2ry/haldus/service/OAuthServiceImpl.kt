package org.featx.sta2ry.haldus.service

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.authentication.Credentials
import io.vertx.ext.auth.oauth2.OAuth2Auth
import io.vertx.ext.auth.oauth2.OAuth2AuthorizationURL
import io.vertx.ext.auth.oauth2.OAuth2Options
import io.vertx.ext.web.client.WebClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuthServiceImpl : OAuthService {

    @Inject
    private lateinit var vertx: Vertx

    private val webClient by lazy { WebClient.create(vertx) }

    override fun getRedirectUrl(provider: String): String {
        val auth = getProvider(provider)
        val authUrl = OAuth2AuthorizationURL()
            .setRedirectUri(getRedirectUri(provider))
            .addScope(getScopes(provider))
            .setState("haldus_state")
        return auth.authorizeURL(authUrl)
    }

    override fun exchangeCode(provider: String, code: String): Future<OAuthUserInfo> {
        val auth = getProvider(provider)
        val redirectUri = getRedirectUri(provider)

        val credentials = Credentials {
            JsonObject()
                .put("code", code)
                .put("redirect_uri", redirectUri)
        }

        return auth.authenticate(credentials)
            .compose { user ->
                val accessToken = user.principal().getString("access_token")
                fetchUserInfo(provider, accessToken)
            }
    }

    private fun fetchUserInfo(
        provider: String,
        accessToken: String
    ): Future<OAuthUserInfo> {
        return when (provider) {
            "github" -> {
                webClient.getAbs("https://api.github.com/user")
                    .putHeader("Authorization", "Bearer $accessToken")
                    .putHeader("Accept", "application/vnd.github.v3+json")
                    .send()
                    .compose { response ->
                        val body = response.bodyAsJsonObject()
                        Future.succeededFuture(
                            OAuthUserInfo(
                                providerUserId = body.getInteger("id").toString(),
                                displayName = body.getString("login"),
                                email = body.getString("email"),
                                avatarUrl = body.getString("avatar_url"),
                            )
                        )
                    }
            }
            "google" -> {
                webClient.getAbs("https://www.googleapis.com/oauth2/v2/userinfo")
                    .putHeader("Authorization", "Bearer $accessToken")
                    .send()
                    .compose { response ->
                        val body = response.bodyAsJsonObject()
                        Future.succeededFuture(
                            OAuthUserInfo(
                                providerUserId = body.getString("id"),
                                displayName = body.getString("name"),
                                email = body.getString("email"),
                                avatarUrl = body.getString("picture"),
                            )
                        )
                    }
            }
            "wechat" -> {
                webClient.getAbs("https://api.weixin.qq.com/sns/userinfo")
                    .addQueryParam("access_token", accessToken)
                    .addQueryParam("openid", "")
                    .send()
                    .compose { response ->
                        val body = response.bodyAsJsonObject()
                        Future.succeededFuture(
                            OAuthUserInfo(
                                providerUserId = body.getString("openid"),
                                displayName = body.getString("nickname"),
                                email = null,
                                avatarUrl = body.getString("headimgurl"),
                            )
                        )
                    }
            }
            else -> Future.failedFuture("Unsupported provider: $provider")
        }
    }

    private fun getProvider(provider: String): OAuth2Auth {
        return when (provider) {
            "github" -> OAuth2Auth.create(
                vertx,
                OAuth2Options()
                    .setClientId("github_client_id")
                    .setClientSecret("github_client_secret")
                    .setSite("https://github.com/login")
                    .setTokenPath("https://github.com/login/oauth/access_token")
                    .setAuthorizationPath("https://github.com/login/oauth/authorize")
            )
            "google" -> OAuth2Auth.create(
                vertx,
                OAuth2Options()
                    .setClientId("google_client_id")
                    .setClientSecret("google_client_secret")
                    .setSite("https://accounts.google.com")
                    .setTokenPath("https://oauth2.googleapis.com/token")
                    .setAuthorizationPath("https://accounts.google.com/o/oauth2/auth")
            )
            "wechat" -> OAuth2Auth.create(
                vertx,
                OAuth2Options()
                    .setClientId("wechat_app_id")
                    .setClientSecret("wechat_app_secret")
                    .setSite("https://api.weixin.qq.com")
                    .setTokenPath("https://api.weixin.qq.com/sns/oauth2/access_token")
                    .setAuthorizationPath("https://open.weixin.qq.com/connect/qrconnect")
            )
            else -> throw IllegalArgumentException("Unsupported provider: $provider")
        }
    }

    private fun getRedirectUri(provider: String): String {
        return "http://localhost:8080/auth/oauth/$provider/callback"
    }

    private fun getScopes(provider: String): String {
        return when (provider) {
            "github" -> "user:email"
            "google" -> "openid profile email"
            "wechat" -> "snsapi_login"
            else -> ""
        }
    }
}
