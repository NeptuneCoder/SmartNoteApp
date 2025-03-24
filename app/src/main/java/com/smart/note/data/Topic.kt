package com.smart.note.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tab_topic")
data class Topic(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("topic_id")
    val topicId: String = "",
    @ColumnInfo("topic_name")
    val topicName: String = "",
    @ColumnInfo("create_time")
    val createTime: Long = System.currentTimeMillis()
)