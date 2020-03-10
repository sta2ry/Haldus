package org.featx.sta2ry.haldus.service

import io.reactivex.Single
import org.featx.sta2ry.haldus.entity.UserEntity

interface UserService {

    fun save(user: UserEntity): Single<UserEntity>

    fun find(key: String, value: String): Single<UserEntity>

    fun find(unique: String): Single<UserEntity>

    fun findByCode(code: String): Single<UserEntity>

    fun findByUsername(usernaame: String): Single<UserEntity>
}
