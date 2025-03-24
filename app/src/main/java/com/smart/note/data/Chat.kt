package com.smart.note.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tab_chat_detail")
data class ChatDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("type")
    val type: Int,
    @ColumnInfo("topic_id")
    val topicId: String = "",
    @ColumnInfo("topic_name")
    val topicName: String = "",
    @ColumnInfo("create_time")
    val createTime: Long = System.currentTimeMillis()
)