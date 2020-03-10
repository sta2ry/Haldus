package org.featx.sta2ry.haldus;


import org.featx.sta2ry.haldus.entity.UserEntity
import org.featx.sta2ry.haldus.transmit.domain.UserData
import org.junit.jupiter.api.Test

class PropertiesAssignmentTest {

    @Test
    fun testPropertiesAssignment() {
        val userDomain = UserData(1, "USR12324", "掌心童话", 0, "palmtale", "3423132121321", false, "")
        val (id, code, name, type, username) = userDomain
        val userEntity = UserEntity(id, code, name, type, username)
        println(userEntity)
    }
}
