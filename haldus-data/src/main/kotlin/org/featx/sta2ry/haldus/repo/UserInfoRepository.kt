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
class UserInfoRepository : UserInfoRepo {

    @Inject
    private lateinit var sqlClient: Pool

    override val connectionPool: Pool
        get() = sqlClient

    private fun toUserInfoEntity(row: Row): UserInfoEntity {
        val result = UserInfoEntity.builder()
            .username(row.getString("username"))
            .enable(row.getBoolean("enable"))
            .email(row.getString("email")).emailVerified(row.getBoolean("email_verified"))
            .phone(row.getString("phone")).phoneVerified(row.getBoolean("phone_verified"))
            .avatar(row.getString("avatar"))
            .build()
        result.id = row.getLong("id")
        result.code = row.getString("code")
        result.name = row.getString("name")
        result.type = row.getInteger("type")
        result.createdAt = row.getLocalDateTime("created_at")
        result.updatedAt = row.getLocalDateTime("updated_at")
        return result
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
            values.add(user.name)
        }
        if (StringUtil.isNotBlank(user.email)) {
            sets.append("email=?,")
            values.add(user.email)
        }
        values.add(user.code)
        return conn.preparedQuery(String.format("update t_user_info set %s where code = ?", sets.toString()))
            .execute(Tuple.from(values))
            .map { it.first().get(Integer::class.java, "row") > 0 }
    }

    override fun delete(code: String, txConnect: SqlClient?): Future<Boolean> {
        val conn = txConnect ?: sqlClient
        return conn.preparedQuery("update t_user_info set deleted = ? where code = ?")
            .execute(Tuple.of(true, code))
            .map { it.first().get(Integer::class.java, "row") > 0 }
    }

    override fun retrieveOne(code: String, txConnect: SqlClient?): Future<UserInfoEntity> {
        val conn = txConnect ?: sqlClient
        return conn.preparedQuery("select * from t_user_info where code = ? and deleted = ? limit ?")
            .execute(Tuple.of(code, false, 1))
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
            .map { it.map(this::toUserInfoEntity) }
    }
}
