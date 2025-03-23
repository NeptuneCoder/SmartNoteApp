package com.smart.note.net

import com.smart.note.model.ChatRequest
import com.smart.note.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("chat/completions")
    suspend fun chatCompletion(
        @Body request: ChatRequest
    ): ChatResponse
}