package org.featx.sta2ry.haldus.handler

import io.reactivex.Single
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext
import org.featx.sta2ry.haldus.entity.UserEntity
import org.featx.sta2ry.haldus.handler.model.user.SessionCreation
import org.featx.sta2ry.haldus.handler.model.user.SessionInfo
import org.featx.sta2ry.haldus.handler.model.user.SessionVerify
import org.featx.sta2ry.haldus.handler.model.user.UserInfo
import org.featx.sta2ry.haldus.service.SessionService
import org.featx.sta2ry.haldus.service.UserService
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserHandler : BaseHandler() {
    @Inject
    private lateinit var userService: UserService
    @Inject
    private lateinit var sessionService: SessionService

    override fun configGetPaths(rc: RoutingContext?) {
        val request = rc?.request()!!
        when (request.path()) {
            "/user/info" -> getUserInfo(request).subscribe(onSuccess(rc), onError(rc))
            "/user/session" -> getUserSession(request).subscribe(onSuccess(rc), onError(rc))
            "/user/token" -> getUserSession(request).subscribe(onSuccess(rc), onError(rc))
            else -> rc.response()?.end()
        }
    }

    override fun configPostPaths(rc: RoutingContext?) {
        val request = rc?.request()!!
        when (request.path()) {
            "/user/info" -> postUserInfo(request).subscribe(onSuccess(rc), onError(rc))
            "/user/session" -> postUserSession(request).subscribe(onSuccess(rc), onError(rc))
            "/user/token" -> postUserSession(request).subscribe(onSuccess(rc), onError(rc))
            else -> rc.response()?.end()
        }
    }

    fun postUserInfo(request: HttpServerRequest): Single<UserInfo> {
        return io.vertx.reactivex.core.http.HttpServerRequest(request).toFlowable().concatMapSingle {
            val userInfo = it.toJsonObject().mapTo(UserInfo::class.java)
            this.userService.save(toUserEntity(userInfo))
        }.map {
            toUserInfo(it)
        }.singleOrError()
    }

    private fun toUserEntity(userInfo: UserInfo): UserEntity {
        val result = UserEntity(
            userInfo.username, "", true,
            userInfo.avatar,
            userInfo.email, false,
            userInfo.phone, false);
        result.code = userInfo.code;
        result.id = userInfo.id
        result.name = userInfo.name
        result.type = userInfo.type
        return result
    }

    fun getUserInfo(request: HttpServerRequest): Single<UserInfo> {
        var code = request.getParam("code")
        if (code == null) {
            code = request.getFormAttribute("code")
        }
        return this.userService.findByCode(code).map {
            toUserInfo(it)
        }
    }

    private fun toUserInfo(userEntity: UserEntity): UserInfo {
        return UserInfo(
            userEntity.id,
            userEntity.code,
            userEntity.name,
            userEntity.type,
            userEntity.username,
            userEntity.avatar,
            userEntity.email,
            userEntity.phone
        )
    }

    fun postUserSession(request: HttpServerRequest): Single<Boolean> {
        return io.vertx.reactivex.core.http.HttpServerRequest(request).toObservable().concatMapSingle {
            val sessionCreation = it.toJsonObject().mapTo(SessionCreation::class.java)
            this.sessionService.create(sessionCreation)
        }.singleOrError()
    }

    fun getUserSession(request: HttpServerRequest): Single<SessionInfo> {
        var key = request.getHeader("key")
        var value = ""
        var code = ""
        if (key == null || "".equals(key.trim())) {
            key = request.getParam("key")
        } else {
            value = request.getHeader("value")
            code = request.getHeader("code")
        }
        if (key == null || "".equals(key.trim())) {
            key = request.getFormAttribute("key")
        } else {
            value = request.getParam("value")
            code = request.getParam("code")
        }
        if (key == null || "".equals(key.trim())) {
            return io.vertx.reactivex.core.http.HttpServerRequest(request).toObservable().concatMapSingle {
                this.sessionService.verify(it.toJsonObject().mapTo(SessionVerify::class.java))
            }.singleOrError()
        } else {
            value = request.getFormAttribute("value")
            code = request.getFormAttribute("code")
        }
        return this.sessionService.verify(SessionVerify(key.toInt(), value, code))
    }
}
