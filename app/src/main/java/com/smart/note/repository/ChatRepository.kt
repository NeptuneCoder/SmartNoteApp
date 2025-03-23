package com.smart.note.repository

import com.smart.note.model.ChatRequest
import com.smart.note.model.ChatResponse
import com.smart.note.model.Message
import com.smart.note.net.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun sendMessage(userMessage: String): Flow<ChatResponse> = flow {
        val request = ChatRequest(
            model = "deepseek-chat",
            messages = listOf(
                Message(role = "system", content = "You are a helpful assistant."),
                Message(role = "user", content = userMessage)
            ),
            stream = false
        )
        emit(apiService.chatCompletion(request = request))

    }
}