package com.smart.note.model

data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean
)

data class Message(
    val role: String,
    val content: String
)