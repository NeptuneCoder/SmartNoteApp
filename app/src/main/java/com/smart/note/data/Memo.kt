package com.smart.note.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tab_memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("content")
    var content: String,
    @ColumnInfo("ai_summary", defaultValue = "")
    var aiSummary: String = "",
    @ColumnInfo("md5")
    var md5: String,
    @ColumnInfo("msg_type")
    val msgType: Int = Type.NOTE.ordinal,
    @ColumnInfo("create_time")
    val createTime: Long = System.currentTimeMillis(),
    @ColumnInfo("update_time")
    var updateTime: Long = System.currentTimeMillis(),

    ) : Serializable