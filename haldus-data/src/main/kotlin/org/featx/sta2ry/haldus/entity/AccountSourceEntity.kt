package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractRecord

class AccountSourceEntity(
    var accountId: Long? = null,
    var service: String? = null,
) : AbstractRecord<Long>()
