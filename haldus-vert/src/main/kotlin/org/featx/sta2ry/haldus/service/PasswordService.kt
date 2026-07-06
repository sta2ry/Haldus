package org.featx.sta2ry.haldus.service

interface PasswordService {
    fun hash(password: String): String
    fun verify(password: String, hash: String): Boolean
}
