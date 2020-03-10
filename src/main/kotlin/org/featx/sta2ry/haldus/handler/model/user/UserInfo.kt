package org.featx.sta2ry.haldus.handler.model.user

import java.io.Serializable

data class UserInfo(
  var id: Long? = null,
  var code: String? = null,
  var name: String? = null,
  var type: Int? = null,
  var username: String? = null,
  var avatar: String? = null,
  var email: String? = null,
  var phone: String? = null
) : Serializable
