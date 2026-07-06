package org.featx.sta2ry.haldus.service

import io.vertx.core.Future
import org.featx.spec.model.PageResponse
import org.featx.sta2ry.haldus.model.*

interface UserInfoService {

    fun create(user: AccountSave): Future<AccountShow>

    fun update(user: AccountSave): Future<AccountShow>

    fun delete(code: String): Future<Boolean>

    fun findByCode(code: String): Future<AccountDetail>

    fun page(page: AccountPageRequest): Future<PageResponse<AccountItem>>
}
