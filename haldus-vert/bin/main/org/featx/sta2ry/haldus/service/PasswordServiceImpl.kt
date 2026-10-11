package org.featx.sta2ry.haldus.service

import at.favre.lib.crypto.bcrypt.BCrypt
import jakarta.inject.Singleton

@Singleton
class PasswordServiceImpl : PasswordService {
    private val verifier = BCrypt.verifyer()
    private val hasher = BCrypt.withDefaults()

    override fun hash(password: String): String {
        return hasher.hashToString(12, password.toCharArray())
    }

    override fun verify(password: String, hash: String): Boolean {
        return verifier.verify(password.toCharArray(), hash).verified
    }
}
