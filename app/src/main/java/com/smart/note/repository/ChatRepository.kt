package com.smart.note.repository

import com.smart.note.model.ChatRequest
import com.smart.note.model.ChatResponse
import com.smart.note.model.Message
import com.smart.note.net.ApiService
import javax.inject.Inject

class ChatRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun sendMessage(userMessage: String): ChatResponse {
        val request = ChatRequest(
            model = "deepseek-chat",
            messages = listOf(
                Message(role = "system", content = "You are a helpful assistant."),
                Message(role = "user", content = userMessage)
            ),
            stream = false
        )

        return try {
            apiService.chatCompletion(request = request)
        } catch (e: Exception) {
            // 处理网络异常
            throw e
        }
    }
}