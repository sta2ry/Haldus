package org.featx.sta2ry.haldus.entity

import org.featx.spec.entity.AbstractRecord

data class UserSourceEntity(
    var service: String? = null,
    var serviceUserCode: String? = null,
    var serviceKey: String? = null,
    var serviceKeyCode: String? = null
) : AbstractRecord<Long>() {
}