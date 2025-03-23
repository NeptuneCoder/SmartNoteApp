package com.smart.note.model

data class ChatResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)


// 流式响应模型（按 SSE 格式解析）
data class StreamResponse(
    val id: String?,
    val `object`: String?,
    val created: Long?,
    val model: String?,
    val choices: List<StreamChoice>?
)

data class StreamChoice(
    val delta: DeltaContent?,
    val index: Int?,
    val finish_reason: String?
)

data class DeltaContent(
    val role: String?,
    val content: String?
)