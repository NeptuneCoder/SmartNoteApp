package com.smart.note.ext

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray(Charsets.UTF_8))
    return BigInteger(1, digest).toString(16).padStart(32, '0')
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatMillisToDateTime(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val zoneId = ZoneId.systemDefault() // 你的时区，可以改成 ZoneId.of("Asia/Shanghai")
    val dateTime = instant.atZone(zoneId)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    return dateTime.format(formatter)
}