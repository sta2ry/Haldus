package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractUnified

class AccountContactEntity(
    var accountId: Long? = null,
    var contact: String? = null,
    var phone: String? = null,
    var phoneVerified: Boolean? = null,
    var defauld: Boolean? = null,
    var distinct: String? = null,
    var address: String? = null,
    var comment: String? = null,
) : AbstractUnified<Long>()
