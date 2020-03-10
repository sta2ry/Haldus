package org.featx.sta2ry.haldus.transmit.repository

import io.reactivex.Completable
import io.reactivex.Single
import org.featx.sta2ry.haldus.transmit.domain.UserData

interface UserRepo {

    fun create(user: UserData): Single<UserData>

    fun update(user: UserData): Completable

    fun delete(id: Long)

    fun retrieveOne(id: Long): Single<UserData>

    fun retrieveOne(key: String, value: Any): Single<UserData>
}
