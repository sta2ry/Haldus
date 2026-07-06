package org.featx.sta2ry.haldus.service

import io.vertx.core.Future
import org.featx.spec.feature.IdGenerate
import org.featx.sta2ry.haldus.criteria.AuthCriteria
import org.featx.sta2ry.haldus.entity.AccountAuthEntity
import org.featx.sta2ry.haldus.entity.AccountOAuthEntity
import org.featx.sta2ry.haldus.model.*
import org.featx.sta2ry.haldus.repo.AuthRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthServiceImpl : AuthService {

    @Inject
    private lateinit var authRepo: AuthRepo

    @Inject
    private lateinit var idGenerate: IdGenerate

    @Inject
    private lateinit var tokenService: TokenService

    @Inject
    private lateinit var passwordService: PasswordService

    @Inject
    private lateinit var oAuthService: OAuthService

    override fun register(request: RegisterRequest): Future<TokenResponse> {
        if (request.email.isNullOrBlank() && request.phone.isNullOrBlank()) {
            return Future.failedFuture("email or phone is required")
        }

        val criteria = AuthCriteria(email = request.email, phone = request.phone)
        return authRepo.findByCriteria(criteria, null).compose { existing ->
            if (existing != null) {
                Future.failedFuture("account already exists")
            } else {
                val entity = AccountAuthEntity(
                    email = request.email,
                    phone = request.phone,
                    passwordHash = passwordService.hash(request.password),
                    displayName = request.displayName,
                    emailVerified = false,
                    phoneVerified = false,
                )
                entity.code = "AUTH" + idGenerate.nextId().toString(36)
                authRepo.createAccount(entity, null).map { created ->
                    tokenService.generateTokens(toUserBrief(created))
                }
            }
        }
    }

    override fun login(request: LoginRequest): Future<TokenResponse> {
        val criteria = AuthCriteria(
            email = request.account,
            phone = request.account
        )
        return authRepo.findByCriteria(criteria, null).compose { entity ->
            if (entity == null) {
                Future.failedFuture("invalid credentials")
            } else if (!passwordService.verify(request.password, entity.passwordHash ?: "")) {
                Future.failedFuture("invalid credentials")
            } else {
                authRepo.updateLastLogin(entity.id!!, null).map {
                    tokenService.generateTokens(toUserBrief(entity))
                }
            }
        }
    }

    override fun refreshToken(request: RefreshRequest): Future<TokenResponse> {
        val userBrief = tokenService.verifyRefreshToken(request.refreshToken)
            ?: return Future.failedFuture("invalid refresh token")

        val entity = AccountAuthEntity(
            email = userBrief.email,
            phone = userBrief.phone,
            displayName = userBrief.displayName,
            avatarUrl = userBrief.avatarUrl,
        )
        entity.id = userBrief.id
        entity.code = userBrief.code

        return authRepo.updateLastLogin(userBrief.id, null).map {
            tokenService.generateTokens(userBrief)
        }
    }

    override fun oauthCallback(request: OAuthCallbackRequest): Future<TokenResponse> {
        return oAuthService.exchangeCode(request.provider, request.code).compose { userInfo ->
            authRepo.findOAuth(request.provider, userInfo.providerUserId, null).compose { existingOAuth ->
                if (existingOAuth != null) {
                    val entity = AccountAuthEntity()
                    entity.id = existingOAuth.userId
                    authRepo.updateLastLogin(existingOAuth.userId!!, null).map {
                        tokenService.generateTokens(
                            UserBrief(
                                id = existingOAuth.userId!!,
                                code = existingOAuth.code,
                                displayName = userInfo.displayName,
                                email = userInfo.email,
                                phone = null,
                                avatarUrl = userInfo.avatarUrl,
                            )
                        )
                    }
                } else {
                    val newAccount = AccountAuthEntity(
                        email = userInfo.email,
                        displayName = userInfo.displayName,
                        avatarUrl = userInfo.avatarUrl,
                        emailVerified = true,
                    )
                    newAccount.code = "AUTH" + idGenerate.nextId().toString(36)
                    authRepo.createAccount(newAccount, null).compose { created ->
                        val oauth = AccountOAuthEntity(
                            userId = created.id,
                            provider = request.provider,
                            providerUserId = userInfo.providerUserId,
                        )
                        oauth.code = "OAU" + idGenerate.nextId().toString(36)
                        authRepo.createOAuth(oauth, null).map {
                            tokenService.generateTokens(toUserBrief(created))
                        }
                    }
                }
            }
        }
    }

    override fun getOAuthRedirectUrl(provider: String): Future<String> {
        return Future.succeededFuture(oAuthService.getRedirectUrl(provider))
    }

    private fun toUserBrief(entity: AccountAuthEntity): UserBrief {
        return UserBrief(
            id = entity.id!!,
            code = entity.code ?: "",
            displayName = entity.displayName,
            email = entity.email,
            phone = entity.phone,
            avatarUrl = entity.avatarUrl,
        )
    }
}
