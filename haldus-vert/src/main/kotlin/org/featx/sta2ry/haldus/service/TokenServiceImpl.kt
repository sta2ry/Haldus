package org.featx.sta2ry.haldus.service

import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.jwt.JWTAuth
import org.featx.sta2ry.haldus.model.TokenResponse
import org.featx.sta2ry.haldus.model.UserBrief
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenServiceImpl : TokenService {

    @Inject
    private lateinit var jwtAuth: JWTAuth

    override fun generateTokens(user: UserBrief): TokenResponse {
        val accessToken = jwtAuth.generateToken(
            JsonObject()
                .put("user_id", user.id)
                .put("code", user.code)
                .put("email", user.email ?: "")
                .put("displayName", user.displayName ?: ""),
            JWTOptions()
                .setIssuer("haldus")
                .setSubject(user.id.toString())
                .setExpiresInSeconds(900)
        )

        val refreshToken = jwtAuth.generateToken(
            JsonObject()
                .put("user_id", user.id)
                .put("code", user.code)
                .put("type", "refresh"),
            JWTOptions()
                .setIssuer("haldus")
                .setSubject(user.id.toString())
                .setExpiresInSeconds(604800)
        )

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = "Bearer",
            expiresIn = 900,
            user = user,
        )
    }

    override fun verifyAccessToken(token: String): UserBrief? {
        return try {
            val user = jwtAuth.authenticate(
                io.vertx.ext.auth.authentication.TokenCredentials(token)
            ).result().principal()
            UserBrief(
                id = user.getLong("user_id"),
                code = user.getString("code"),
                displayName = user.getString("displayName"),
                email = user.getString("email"),
                phone = user.getString("phone"),
                avatarUrl = user.getString("avatarUrl"),
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun verifyRefreshToken(token: String): UserBrief? {
        return try {
            val user = jwtAuth.authenticate(
                io.vertx.ext.auth.authentication.TokenCredentials(token)
            ).result().principal()
            if (user.getString("type") != "refresh") {
                return null
            }
            UserBrief(
                id = user.getLong("user_id"),
                code = user.getString("code"),
                displayName = user.getString("displayName"),
                email = user.getString("email"),
                phone = user.getString("phone"),
                avatarUrl = user.getString("avatarUrl"),
            )
        } catch (e: Exception) {
            null
        }
    }
}
