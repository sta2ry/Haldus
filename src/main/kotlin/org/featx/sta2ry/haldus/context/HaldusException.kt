package org.featx.sta2ry.haldus.context

data class HaldusException(
    var code: String,
    override var message: String,
    var tips: String
) : Exception(message) {
    constructor(errorEnum: ErrorEnum, tips: String) : this(errorEnum.getCode(), errorEnum.getMessage(), tips)
    constructor(errorEnum: ErrorEnum) : this(errorEnum.getCode(), errorEnum.getMessage(), "")
}
