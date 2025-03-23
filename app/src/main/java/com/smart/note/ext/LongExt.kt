package com.smart.note.ext

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.formatMillisToDateTime(): String {
    val instant = Instant.ofEpochMilli(this)
    val zoneId = ZoneId.systemDefault() // 你的时区，可以改成 ZoneId.of("Asia/Shanghai")
    val dateTime = instant.atZone(zoneId)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return dateTime.format(formatter)
}