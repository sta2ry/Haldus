package org.featx.sta2ry.haldus.repo

import io.vertx.core.Future
import io.vertx.sqlclient.SqlClient
import org.featx.spec.model.PageRequest
import org.featx.sta2ry.haldus.criteria.UserInfoCriteria
import org.featx.sta2ry.haldus.entity.UserInfoEntity

interface AccountRepo : TransactionConnectionHold {

    fun create(account: UserInfoEntity, txConnect: SqlClient?): Future<UserInfoEntity>

    fun update(account: UserInfoEntity, txConnect: SqlClient?): Future<Boolean>

    fun delete(id: Long, txConnect: SqlClient?): Future<Boolean>

    fun retrieveOne(id: Long, txConnect: SqlClient?): Future<UserInfoEntity>

    fun count(criteria: UserInfoCriteria, txConnect: SqlClient?): Future<Long>

    fun page(criteria: UserInfoCriteria, page: PageRequest, txConnect: SqlClient?): Future<List<UserInfoEntity>>
}
