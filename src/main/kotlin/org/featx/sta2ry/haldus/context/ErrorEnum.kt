package org.featx.sta2ry.haldus.context

enum class ErrorEnum {
    EntityNotFound("4041", "EntityNotFound"),
    ;

    private var code: String
    private var message: String

    fun getCode(): String {
        return code
    }

    fun getMessage(): String {
        return message;
    }

    constructor(code: String, message: String) {
        this.code = code
        this.message = message
    }
}