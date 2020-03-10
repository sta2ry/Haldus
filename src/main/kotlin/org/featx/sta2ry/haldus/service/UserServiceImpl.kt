package org.featx.sta2ry.haldus.service;

import io.reactivex.Single
import org.featx.sta2ry.haldus.entity.UserEntity
import org.featx.sta2ry.haldus.transmit.domain.UserData
import org.featx.sta2ry.haldus.transmit.repository.UserRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserServiceImpl : UserService {

    @Inject
    lateinit var userRepo: UserRepo

    private fun toUserData(user: UserEntity): UserData {
        return UserData(
            user.id,
            user.code,
            user.name,
            user.type,
            user.username,
            user.password,
            user.enable,
            user.avatar,
            user.email,
            user.emailVerified,
            user.phone,
            user.emailVerified
        )
    }

    override fun save(user: UserEntity): Single<UserEntity> {
        return userRepo.create(toUserData(user)).map {
            toUserEntity(it)
        }
    }

    private fun toUserEntity(userData: UserData): UserEntity {
        return UserEntity(
            userData.id,
            userData.code,
            userData.name,
            userData.type,
            userData.username,
            userData.password,
            userData.enable,
            userData.avatar,
            userData.email,
            userData.emailVerified,
            userData.phone,
            userData.emailVerified
        )
    }

    override fun find(key: String, value: String): Single<UserEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(unique: String): Single<UserEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByCode(code: String): Single<UserEntity> {
        return userRepo.retrieveOne("code", code).map {
            toUserEntity(it)
        }!!
    }

    override fun findByUsername(usernaame: String): Single<UserEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
