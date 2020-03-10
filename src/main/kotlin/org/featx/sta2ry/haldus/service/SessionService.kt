package org.featx.sta2ry.haldus.service;

import io.reactivex.Single
import org.featx.sta2ry.haldus.handler.model.user.*

interface SessionService {

    fun create(session: SessionCreation): Single<Boolean>

    fun verify(session: SessionVerify): Single<SessionInfo>

    fun refresh(session: SessionUpdate): Single<SessionInfo>

    fun terminate(session: SessionDelete): Single<Boolean>

}
