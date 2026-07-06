package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractUnified

class AccountEntity(
    var status: Boolean? = null,
) : AbstractUnified<Long>()
