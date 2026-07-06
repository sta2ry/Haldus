package org.featx.sta2ry.haldus.repo

import io.vertx.sqlclient.Pool

/**
 * @author excepts
 */
interface TransactionConnectionHold {
    val connectionPool: Pool
}