package com.smart.note.model

data class ChatContent(
    val type: Int,
    val content: String,
    val createTime: Long = System.currentTimeMillis()
)