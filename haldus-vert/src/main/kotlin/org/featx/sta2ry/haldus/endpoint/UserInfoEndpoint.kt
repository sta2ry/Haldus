package org.featx.sta2ry.haldus.endpoint

import io.vertx.core.Future
import org.featx.spec.model.PageResponse
import org.featx.sta2ry.haldus.handler.UserInfoHandler
import org.featx.sta2ry.haldus.model.*
import org.featx.sta2ry.haldus.service.UserInfoService
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class UserInfoEndpoint : UserInfoHandler {
    @Inject
    private lateinit var userInfoService: UserInfoService

    override fun postUserInfo(accountSave: AccountSave): Future<AccountShow> {
        return userInfoService.create(accountSave)
    }

    override fun getUserInfo(userCode: String): Future<AccountDetail> {
        return userInfoService.findByCode(userCode)
    }

    override fun deleteUserInfo(userCode: String): Future<Boolean> {
        return userInfoService.delete(userCode)
    }

    override fun page(accountPageRequest: AccountPageRequest): Future<PageResponse<AccountItem>> {
        return userInfoService.page(accountPageRequest)
    }
}