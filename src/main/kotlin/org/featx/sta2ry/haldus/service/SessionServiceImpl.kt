package org.featx.sta2ry.haldus.service

import io.reactivex.Single
import io.vertx.core.json.Json
import io.vertx.reactivex.redis.client.Redis
import org.featx.sta2ry.haldus.enums.CacheKey
import org.featx.sta2ry.haldus.handler.model.user.*
import org.featx.sta2ry.haldus.transmit.repository.UserRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionServiceImpl: SessionService {

    @Inject
    lateinit var userRepo: UserRepo
    @Inject
    lateinit var redisClient: Redis

    override fun create(session: SessionCreation): Single<Boolean> {
        redisClient.rxSend(CacheKey.USER_SESSION.getContent(), "", Json.encode(session)).map {
            redisClient.rxSend("ddd", "yep")
        }.map {

        }
        redisClient.rxHget("hset", "key").map {

        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun verify(session: SessionVerify): Single<SessionInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refresh(session: SessionUpdate): Single<SessionInfo> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun terminate(session: SessionDelete): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
