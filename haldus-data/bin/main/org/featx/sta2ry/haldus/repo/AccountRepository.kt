package org.featx.sta2ry.haldus.repo

import io.vertx.core.Future
import io.vertx.mysqlclient.MySQLClient.LAST_INSERTED_ID
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import org.featx.spec.model.PageRequest
import org.featx.spec.util.StringUtil
import org.featx.sta2ry.haldus.criteria.UserInfoCriteria
import org.featx.sta2ry.haldus.entity.UserInfoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository : AccountRepo {

    @Inject
    private lateinit var sqlClient: Pool

    override val connectionPool: Pool
        get() = sqlClient

    private fun toUserInfoEntity(row: Row): UserInfoEntity {
        val entity = UserInfoEntity(
            username = row.getString("username"),
            enable = row.getBoolean("enable"),
            email = row.getString("email"),
            phone = row.getString("phone"),
            emailVerified = row.getBoolean("email_verified"),
            phoneVerified = row.getBoolean("phone_verified"),
            avatar = row.getString("avatar"),
            password = row.getString("password"),
        )
        entity.id = row.getLong("id")
        entity.code = row.getString("code")
        entity.name = row.getString("name")
        entity.type = row.getInteger("type")
        entity.createdAt = row.getLocalDateTime("created_at")
        entity.updatedAt = row.getLocalDateTime("updated_at")
        return entity
    }

    override fun create(user: UserInfoEntity, txConnect: SqlClient?): Future<UserInfoEntity> {
        val conn = txConnect ?: sqlClient
        return conn.preparedQuery(
            "insert into t_user_info(" +
                    "`code`, `name`, `type`, `username`, `password`, `enable`, `avatar`,`email`, `phone`, " +
                    "`email_verified`, `phone_verified`) values(?,?,?,?,?,?,?,?,?,?,?)"
        )
            .execute(
                Tuple.of(
                    user.code, user.name, user.type, user.username,
                    user.password, user.enable, user.avatar, user.email, user.phone,
                    user.emailVerified, user.phoneVerified
                )
            )
            .map {
                user.id = it.property(LAST_INSERTED_ID)
                user
            }
    }

    override fun update(user: UserInfoEntity, txConnect: SqlClient?): Future<Boolean> {
        val conn = txConnect ?: sqlClient
        val sets = StringBuilder()
        val values = mutableListOf<Any>()
        if (StringUtil.isNotBlank(user.name)) {
            sets.append("name=?,")
            values.add(user.name as Any)
        }
        if (StringUtil.isNotBlank(user.email)) {
            sets.append("email=?,")
            values.add(user.email as Any)
        }
        values.add(user.code as Any)
        return conn.preparedQuery("update t_user_info set $sets where code = ?")
            .execute(Tuple.from(values))
            .map { it.first().get(Integer::class.java, "row") > 0 }
    }

    override fun delete(id: Long, txConnect: SqlClient?): Future<Boolean> {
        val conn = txConnect ?: sqlClient
        return conn.preparedQuery("update t_user_info set deleted = ? where id = ?")
            .execute(Tuple.of(true, id))
            .map { it.first().get(Integer::class.java, "row") > 0 }
    }

    override fun retrieveOne(id: Long, txConnect: SqlClient?): Future<UserInfoEntity> {
        val conn = txConnect ?: sqlClient
        return conn.preparedQuery("select * from t_user_info where id = ? and deleted = ? limit ?")
            .execute(Tuple.of(id, false, 1))
            .map { toUserInfoEntity(it.first()) }
    }

    override fun count(criteria: UserInfoCriteria, txConnect: SqlClient?): Future<Long> {
        val conn = txConnect ?: sqlClient
        return conn.preparedQuery("select count(*) result from t_user_info where code = ? and deleted = ?")
            .execute(Tuple.of(criteria.code, false))
            .map { it.first().get(Long::class.java, "result") }
    }

    override fun page(
        criteria: UserInfoCriteria,
        page: PageRequest,
        txConnect: SqlClient?
    ): Future<List<UserInfoEntity>> {
        val conn = txConnect ?: sqlClient
        return conn.preparedQuery("select * from t_user_info where code = ? and deleted = ? limit ?,?")
            .execute(Tuple.of(criteria.code, false, page.offset, page.size))
            .map { rows -> rows.map { row -> toUserInfoEntity(row) } }
    }
}
