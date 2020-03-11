package org.featx.sta2ry.haldus.service

import io.reactivex.Single
import io.vertx.core.json.Json
import io.vertx.reactivex.redis.client.Command
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.Request
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
        redisClient.rxSend(
                Request.cmd(Command.HSET)
                    .arg(CacheKey.USER_SESSION.getContent())
                    .arg("").arg(Json.encode(session))
            )
            .map {
                redisClient.rxSend(Request.cmd(Command.HGET).arg("ddd").arg("yep"))
            }.map {

            }
        redisClient.rxSend(Request.cmd(Command.HSET).arg("key")).map {

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
