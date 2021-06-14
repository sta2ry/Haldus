package org.featx.sta2ry.haldus.service

import io.vertx.core.Future
import org.featx.spec.model.PageResponse
import org.featx.sta2ry.haldus.model.*

interface UserInfoService {

    fun create(user: UserInfoSave): Future<UserInfoShow>

    fun update(user: UserInfoSave): Future<UserInfoShow>

    fun delete(code: String): Future<Boolean>

    fun findByCode(code: String): Future<UserInfoDetail>

    fun page(page: UserInfoPageRequest): Future<PageResponse<UserInfoItem>>
}
