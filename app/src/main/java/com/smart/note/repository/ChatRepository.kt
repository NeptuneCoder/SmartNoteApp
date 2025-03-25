package com.smart.note.repository

import android.util.Log
import com.google.gson.Gson
import com.smart.note.model.ChatRequest
import com.smart.note.model.ChatResponse
import com.smart.note.model.Message
import com.smart.note.model.StreamResponse
import com.smart.note.net.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
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

    fun chatStream(userMessage: String) {
        try {
            val request = ChatRequest(
                model = "deepseek-chat",
                messages = listOf(
                    Message(role = "system", content = "You are a helpful assistant."),
                    Message(role = "user", content = userMessage)
                ),
                stream = false
            )
            val response = apiService.streamChat(
                request = request
            ).execute()

            if (response.isSuccessful) {
                processStream(response.body()!!)
            } else {
                // 处理错误
                Log.i("content", "content 失败了")
            }
        } catch (e: Exception) {
            // 处理异常
            Log.i("content", "content ==== $e")
        }
    }


    private fun processStream(body: ResponseBody) {
        val source = body.source()
        try {
            while (!source.exhausted()) {
                val line = source.readUtf8Line() // 读取一行数据
                line?.let {
                    if (it.startsWith("data: ")) {
                        val json = it.substring(6).trim()
                        if (json == "[DONE]") {
                            // 流结束
                            Log.i("content", "content ==== DONE")
                            return
                        }
                        parseAndEmitContent(json)
                    }
                }
            }
        } finally {
            body.close()
        }
    }

    private fun parseAndEmitContent(json: String) {
        try {
            val response = Gson().fromJson(json, StreamResponse::class.java)
            val content = response.choices?.firstOrNull()?.delta?.content
            content?.let {
                // 更新 UI（通过 LiveData/Flow）
//                _chatContent.emit(content)
                Log.i("content", "content ==== " + content)
            }
        } catch (e: Exception) {
            // 解析错误处理
        }
    }
}