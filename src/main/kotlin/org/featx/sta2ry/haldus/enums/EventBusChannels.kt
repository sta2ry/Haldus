package org.featx.sta2ry.haldus.enums

enum class EventBusChannels(private var value: Int, private var content: String) {
    CONFIGURATION_CHANGED(1, "USN"),
    ;
}
