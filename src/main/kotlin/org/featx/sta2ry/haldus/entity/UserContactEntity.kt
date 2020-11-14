package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractUnified

data class UserContactEntity(
    var contact: String? = null,
    var phone: String? = null,
    var phoneVerified: Boolean = false,
    var userCode: String,
    var default: Boolean = false,
    var district: String? = null,
    var address: String? = null,
    var comment: String ? = null
) : AbstractUnified<Long>() {
}