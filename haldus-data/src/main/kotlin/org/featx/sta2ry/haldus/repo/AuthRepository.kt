package org.featx.sta2ry.haldus.repo

import io.vertx.core.Future
import io.vertx.mysqlclient.MySQLClient.LAST_INSERTED_ID
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import org.featx.spec.feature.IdGenerate
import org.featx.sta2ry.haldus.criteria.AuthCriteria
import org.featx.sta2ry.haldus.entity.AccountAuthEntity
import org.featx.sta2ry.haldus.entity.AccountOAuthEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository : AuthRepo {

    @Inject
    private lateinit var sqlClient: Pool

    @Inject
    private lateinit var idGenerate: IdGenerate

    override val connectionPool: Pool
        get() = sqlClient

    private fun toAccountAuthEntity(row: Row): AccountAuthEntity {
        val entity = AccountAuthEntity(
            email = row.getString("email"),
            phone = row.getString("phone"),
            passwordHash = row.getString("password_hash"),
            displayName = row.getString("display_name"),
            avatarUrl = row.getString("avatar_url"),
            emailVerified = row.getBoolean("email_verified"),
            phoneVerified = row.getBoolean("phone_verified"),
            lastLoginAt = row.getLocalDateTime("last_login_at"),
        )
        entity.id = row.getLong("id")
        entity.code = row.getString("code")
        entity.name = row.getString("name")
        entity.type = row.getInteger("type")
        entity.status = row.getInteger("status")
        entity.deleted = row.getBoolean("deleted")
        entity.createdAt = row.getLocalDateTime("created_at")
        entity.updatedAt = row.getLocalDateTime("updated_at")
        return entity
    }

    private fun toAccountOAuthEntity(row: Row): AccountOAuthEntity {
        val entity = AccountOAuthEntity(
            userId = row.getLong("user_id"),
            provider = row.getString("provider") ?: "",
            providerUserId = row.getString("provider_user_id") ?: "",
            accessToken = row.getString("access_token"),
            refreshToken = row.getString("refresh_token"),
            tokenExpiresAt = row.getLocalDateTime("token_expires_at"),
            providerData = row.getString("provider_data"),
        )
        entity.id = row.getLong("id")
        entity.code = row.getString("code")
        entity.name = row.getString("name")
        entity.type = row.getInteger("type")
        entity.status = row.getInteger("status")
        entity.deleted = row.getBoolean("deleted")
        entity.createdAt = row.getLocalDateTime("created_at")
        entity.updatedAt = row.getLocalDateTime("updated_at")
        return entity
    }

    override fun createAccount(account: AccountAuthEntity, tx: SqlClient?): Future<AccountAuthEntity> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "INSERT INTO account_auth (code, email, phone, password_hash, display_name, " +
                    "avatar_url, email_verified, phone_verified, status, last_login_at, created_at, updated_at) " +
                    "VALUES (?,?,?,?,?,?,?,?,1,?,NOW(),NOW())"
        )
            .execute(
                Tuple.of(
                    account.code, account.email, account.phone, account.passwordHash,
                    account.displayName, account.avatarUrl, account.emailVerified, account.phoneVerified,
                    account.lastLoginAt
                )
            )
            .map {
                account.id = it.property(LAST_INSERTED_ID)
                account
            }
    }

    override fun findByCriteria(criteria: AuthCriteria, tx: SqlClient?): Future<AccountAuthEntity?> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "SELECT * FROM account_auth WHERE deleted = 0 AND " +
                    "((? IS NOT NULL AND email = ?) OR (? IS NOT NULL AND phone = ?)) LIMIT 1"
        )
            .execute(Tuple.of(criteria.email, criteria.email, criteria.phone, criteria.phone))
            .map { rs ->
                val rows = rs.toList()
                if (rows.isNotEmpty()) toAccountAuthEntity(rows[0]) else null
            }
    }

    override fun findById(id: Long, tx: SqlClient?): Future<AccountAuthEntity?> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "SELECT * FROM account_auth WHERE id = ? AND deleted = 0 LIMIT 1"
        )
            .execute(Tuple.of(id))
            .map { rs ->
                val rows = rs.toList()
                if (rows.isNotEmpty()) toAccountAuthEntity(rows[0]) else null
            }
    }

    override fun updateAccount(account: AccountAuthEntity, tx: SqlClient?): Future<Boolean> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "UPDATE account_auth SET display_name = ?, avatar_url = ?, email_verified = ?, " +
                    "phone_verified = ?, updated_at = NOW() WHERE id = ? AND deleted = 0"
        )
            .execute(
                Tuple.of(
                    account.displayName, account.avatarUrl,
                    account.emailVerified, account.phoneVerified, account.id
                )
            )
            .map { it.rowCount() > 0 }
    }

    override fun updateLastLogin(id: Long, tx: SqlClient?): Future<Boolean> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "UPDATE account_auth SET last_login_at = NOW(), updated_at = NOW() WHERE id = ? AND deleted = 0"
        )
            .execute(Tuple.of(id))
            .map { it.rowCount() > 0 }
    }

    override fun createOAuth(oauth: AccountOAuthEntity, tx: SqlClient?): Future<AccountOAuthEntity> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "INSERT INTO account_oauth (code, user_id, provider, provider_user_id, " +
                    "access_token, refresh_token, token_expires_at, provider_data, " +
                    "status, created_at, updated_at) " +
                    "VALUES (?,?,?,?,?,?,?,?,1,NOW(),NOW())"
        )
            .execute(
                Tuple.of(
                    oauth.code, oauth.userId, oauth.provider, oauth.providerUserId,
                    oauth.accessToken, oauth.refreshToken, oauth.tokenExpiresAt, oauth.providerData
                )
            )
            .map {
                oauth.id = it.property(LAST_INSERTED_ID)
                oauth
            }
    }

    override fun findOAuth(
        provider: String,
        providerUserId: String,
        tx: SqlClient?
    ): Future<AccountOAuthEntity?> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "SELECT * FROM account_oauth WHERE provider = ? AND provider_user_id = ? AND deleted = 0 LIMIT 1"
        )
            .execute(Tuple.of(provider, providerUserId))
            .map { rs ->
                val rows = rs.toList()
                if (rows.isNotEmpty()) toAccountOAuthEntity(rows[0]) else null
            }
    }

    override fun findOAuthsByUserId(userId: Long, tx: SqlClient?): Future<List<AccountOAuthEntity>> {
        val conn = tx ?: sqlClient
        return conn.preparedQuery(
            "SELECT * FROM account_oauth WHERE user_id = ? AND deleted = 0"
        )
            .execute(Tuple.of(userId))
            .map { rs -> rs.map { row -> toAccountOAuthEntity(row) } }
    }
}
