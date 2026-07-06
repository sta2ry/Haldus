package org.featx.sta2ry.haldus.util

import org.featx.sta2ry.haldus.criteria.UserInfoCriteria
import org.featx.sta2ry.haldus.entity.UserInfoEntity
import org.featx.sta2ry.haldus.model.AccountDetail
import org.featx.sta2ry.haldus.model.AccountItem
import org.featx.sta2ry.haldus.model.AccountPageRequest
import org.featx.sta2ry.haldus.model.AccountSave
import org.featx.sta2ry.haldus.model.AccountShow

object UserConvertUtil {

    fun toUserInfoEntity(save: AccountSave): UserInfoEntity {
        return UserInfoEntity(
            username = save.username,
            password = save.password,
            email = save.email,
            phone = save.phone,
            avatar = save.avatar,
        ).apply {
            code = save.code
            name = save.name
        }
    }

    fun toUserInfoShow(entity: UserInfoEntity): AccountShow {
        return AccountShow().apply {
            code = entity.code
            name = entity.name
            avatar = entity.avatar
            email = entity.email
            phone = entity.phone
            createdAt = entity.createdAt
            updatedAt = entity.updatedAt
        }
    }

    fun toUserInfoShow(saved: UserInfoEntity, old: UserInfoEntity): AccountShow {
        return AccountShow().apply {
            code = saved.code ?: old.code
            name = saved.name ?: old.name
            avatar = saved.avatar ?: old.avatar
            email = saved.email ?: old.email
            phone = saved.phone ?: old.phone
            createdAt = old.createdAt
            updatedAt = saved.updatedAt ?: old.updatedAt
        }
    }

    fun toUserInfoDetail(entity: UserInfoEntity): AccountDetail {
        return AccountDetail().apply {
            code = entity.code
            name = entity.name
            avatar = entity.avatar
            email = entity.email
            phone = entity.phone
            createdAt = entity.createdAt
            updatedAt = entity.updatedAt
        }
    }

    fun toUserInfoItem(entity: UserInfoEntity): AccountItem {
        return AccountItem().apply {
            code = entity.code
            name = entity.name
            avatar = entity.avatar
            email = entity.email
            phone = entity.phone
            createdAt = entity.createdAt
            updatedAt = entity.updatedAt
        }
    }

    fun toUserInfoCriteria(page: AccountPageRequest): UserInfoCriteria {
        return UserInfoCriteria(
            code = page.code,
        )
    }
}
