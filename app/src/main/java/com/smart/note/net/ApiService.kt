package com.smart.note.net

import com.smart.note.model.ChatRequest
import com.smart.note.model.ChatResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("chat/completions")
    suspend fun chatCompletion(
        @Body request: ChatRequest
    ): ChatResponse

    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    fun streamChat(
        @Body request: ChatRequest
    ): Call<ResponseBody> // 直接返回 ResponseBody

}