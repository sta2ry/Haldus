package org.featx.sta2ry.haldus.repo

import io.vertx.core.Future
import io.vertx.sqlclient.SqlClient
import org.featx.spec.model.PageRequest
import org.featx.sta2ry.haldus.criteria.UserInfoCriteria
import org.featx.sta2ry.haldus.entity.UserInfoEntity


interface UserInfoRepo : TransactionConnectionHold {

    fun create(user: UserInfoEntity, txConnect: SqlClient?): Future<UserInfoEntity>

    fun update(user: UserInfoEntity, txConnect: SqlClient?): Future<Boolean>

    fun delete(code: String, txConnect: SqlClient?): Future<Boolean>

    fun retrieveOne(code: String, txConnect: SqlClient?): Future<UserInfoEntity>

    fun count(criteria: UserInfoCriteria, txConnect: SqlClient?): Future<Long>

    fun page(criteria: UserInfoCriteria, page: PageRequest, txConnect: SqlClient?): Future<List<UserInfoEntity>>

}
