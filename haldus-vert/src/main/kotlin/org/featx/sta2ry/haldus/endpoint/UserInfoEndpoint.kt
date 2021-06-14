package org.featx.sta2ry.haldus.endpoint

import io.vertx.core.Future
import org.featx.spec.model.PageResponse
import org.featx.sta2ry.haldus.handler.UserInfoHandler
import org.featx.sta2ry.haldus.model.*
import org.featx.sta2ry.haldus.service.UserInfoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoEndpoint : UserInfoHandler {
    @Inject
    private lateinit var userInfoService: UserInfoService

    override fun postUserInfo(userInfoSave: UserInfoSave): Future<UserInfoShow> {
        return userInfoService.create(userInfoSave)
    }

    override fun getUserInfo(userCode: String): Future<UserInfoDetail> {
        return userInfoService.findByCode(userCode)
    }

    override fun deleteUserInfo(userCode: String): Future<Boolean> {
        return userInfoService.delete(userCode)
    }

    override fun page(userInfoPageRequest: UserInfoPageRequest): Future<PageResponse<UserInfoItem>> {
        return userInfoService.page(userInfoPageRequest)
    }
}