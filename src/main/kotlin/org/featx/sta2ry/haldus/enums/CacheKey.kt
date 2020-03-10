package org.featx.sta2ry.haldus.enums;

enum class CacheKey(private var value: Int, private var content: String) {
    USER_SESSION(1, "USN"),
    ;
    fun getContent(): String {
        return this.content
    }
}