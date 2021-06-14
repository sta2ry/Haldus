package org.featx.sta2ry.haldus.service

import com.google.common.collect.Lists
import io.vertx.core.Future
import org.featx.spec.feature.IdGenerate
import org.featx.spec.model.PageResponse
import org.featx.spec.util.StringUtil
import org.featx.sta2ry.haldus.model.*
import org.featx.sta2ry.haldus.repo.UserInfoRepo
import org.featx.sta2ry.haldus.util.UserConvertUtil
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoServiceImpl : UserInfoService {

    @Inject
    private lateinit var userInfoRepo: UserInfoRepo

    @Inject
    private lateinit var idGenerator: IdGenerate

    override fun create(user: UserInfoSave): Future<UserInfoShow> {
        if (StringUtil.isBlank(user.code)) {
            user.code = String.format("%s%s", "UIF", idGenerator.nextId().toString(36))
        }
        val userInfoEntity = UserConvertUtil.toUserInfoEntity(user)
        return userInfoRepo.create(userInfoEntity, null)
            .map { UserConvertUtil.toUserInfoShow(it) }
    }

    override fun update(user: UserInfoSave): Future<UserInfoShow> {
        val userInfoEntity = UserConvertUtil.toUserInfoEntity(user)
        return userInfoRepo.connectionPool.withTransaction { tx ->
            userInfoRepo.retrieveOne(user.code, tx).map {
                userInfoRepo.update(userInfoEntity, tx)
                UserConvertUtil.toUserInfoShow(userInfoEntity, it)
            }
        }
    }

    override fun delete(code: String): Future<Boolean> {
        return userInfoRepo.delete(code, null)
    }

    override fun findByCode(code: String): Future<UserInfoDetail> {
        return userInfoRepo.retrieveOne(code, null).map {
            UserConvertUtil.toUserInfoDetail(it)
        }
    }

    override fun page(page: UserInfoPageRequest): Future<PageResponse<UserInfoItem>> {
        val userCriteria = UserConvertUtil.toUserInfoCriteria(page)
        return userInfoRepo.connectionPool.withTransaction { tx ->
            val total = AtomicLong(0L)
            userInfoRepo.count(userCriteria, tx).compose { count ->
                total.set(count)
                if (count == 0L) {
                    Future.succeededFuture(Lists.newArrayList())
                } else {
                    userInfoRepo.page(userCriteria, page, tx)
                }
            }.map {
                PageResponse.succeeded(it.stream().map(UserConvertUtil::toUserInfoItem).collect(Collectors.toList()))
                    .page(page.page).total(total.get())
            }
        }
    }
}