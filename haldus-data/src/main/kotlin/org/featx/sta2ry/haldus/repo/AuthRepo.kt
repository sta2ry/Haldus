package org.featx.sta2ry.haldus.repo

import io.vertx.core.Future
import io.vertx.sqlclient.SqlClient
import org.featx.sta2ry.haldus.criteria.AuthCriteria
import org.featx.sta2ry.haldus.entity.AccountAuthEntity
import org.featx.sta2ry.haldus.entity.AccountOAuthEntity

interface AuthRepo : TransactionConnectionHold {
    fun createAccount(account: AccountAuthEntity, tx: SqlClient?): Future<AccountAuthEntity>
    fun findByCriteria(criteria: AuthCriteria, tx: SqlClient?): Future<AccountAuthEntity?>
    fun findById(id: Long, tx: SqlClient?): Future<AccountAuthEntity?>
    fun updateAccount(account: AccountAuthEntity, tx: SqlClient?): Future<Boolean>
    fun updateLastLogin(id: Long, tx: SqlClient?): Future<Boolean>

    fun createOAuth(oauth: AccountOAuthEntity, tx: SqlClient?): Future<AccountOAuthEntity>
    fun findOAuth(provider: String, providerUserId: String, tx: SqlClient?): Future<AccountOAuthEntity?>
    fun findOAuthsByUserId(userId: Long, tx: SqlClient?): Future<List<AccountOAuthEntity>>
}
