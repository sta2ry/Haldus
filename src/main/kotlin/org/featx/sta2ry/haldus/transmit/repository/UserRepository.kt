package org.featx.sta2ry.haldus.transmit.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.json.JsonArray
import io.vertx.reactivex.ext.sql.SQLClient
import io.vertx.reactivex.ext.sql.SQLConnection
import org.featx.sta2ry.haldus.context.ErrorEnum
import org.featx.sta2ry.haldus.context.HaldusException
import org.featx.sta2ry.haldus.transmit.domain.UserData
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository : UserRepo {

    @Inject
    private lateinit var sqlClient: SQLClient

    private fun toUserData(jsonArray: JsonArray): UserData {
        return UserData(
            jsonArray.getLong(0),
            jsonArray.getString(1),
            jsonArray.getString(2),
            jsonArray.getInteger(3),
            jsonArray.getString(4),
            jsonArray.getString(5),
            !0.equals(jsonArray.getInteger(6)),
            jsonArray.getString(7),
            jsonArray.getString(8),
            !0.equals(jsonArray.getInteger(9)),
            jsonArray.getString(10),
            !0.equals(jsonArray.getInteger(11)),
            !0.equals(jsonArray.getInteger(12)),
            LocalDateTime.parse(jsonArray.getString(13)),
            LocalDateTime.parse(jsonArray.getString(14))
        )
    }

    override fun create(user: UserData): Single<UserData> {
        var con: SQLConnection?
        return transaction {
            con = it
            con?.rxUpdateWithParams(
                "Insert into t_user (code, name, type, username, password, enable, avatar, " +
                        "email, email_verified, phone, phone_verified) values (?,?,?,?,?,?,?,?,?,?,?)",
                JsonArray().add(user.code).add(user.name).add(user.type).add(user.username).add(user.password)
                    .add(user.enable).add(user.avatar).add(user.email).add(user.emailVerified).add(user.phone)
                    .add(user.phoneVerified)
            )?.flatMap {
                con?.rxQuery("select last_insert_id() as id")
            }?.map {
                user.id = it.rows[0].getLong("id")
                user
            }!!
        }
    }

    private fun <T> transaction(function: (SQLConnection) -> Single<T>): Single<T> {
        var con: SQLConnection? = null
        return sqlClient.rxGetConnection().map {
            con = it
            it.rxSetAutoCommit(false)
        }.flatMap {
            function(con!!)
        }.map {
            con?.rxCommit()
            it
        }.doOnError {
            con?.rxRollback()
        }
    }

    override fun update(user: UserData): Completable {
        var con: SQLConnection? = null
        return sqlClient.rxGetConnection().map {
            con = it
        }.map {
            con?.rxSetAutoCommit(false)
        }.flatMap {
            con?.rxUpdateWithParams(
                "Update set into t_user (code, name, type, email, phone) values (?,?,?,?,?)",
                JsonArray().add(user.code).add(user.name).add(user.type).add(user.email).add(user.phone)
            )
        }.flatMapCompletable {
            con?.rxCommit()
        }.doOnError {
            con?.rxRollback()
        }
    }

    override fun delete(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retrieveOne(id: Long): Single<UserData> {
        return retrieveOne("id", id)
    }

    override fun retrieveOne(key: String, value: Any): Single<UserData> {
        return Single.create {
            val emitter = it
            sqlClient.querySingleWithParams(
                "select id, code, name, type, username, password, enable, avatar, " +
                        "email, email_verified, phone, phone_verified, deleted, created_at, updated_at " +
                        "from t_user where " + key + " = ? and deleted = 0 limit 1", JsonArray().add(value)
            ) {
                if (it.failed()) {
                    emitter.onError(it.cause())
                } else if (it.result() == null) {
                    emitter.onError(HaldusException(ErrorEnum.EntityNotFound))
                } else {
                    emitter.onSuccess(toUserData(it.result()))
                }
            }
        }
    }
}
