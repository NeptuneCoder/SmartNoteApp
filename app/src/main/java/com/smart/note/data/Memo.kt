package com.smart.note.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tab_memo")
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("md5")
    val md5: String,
    @ColumnInfo("msg_type")
    val msgType: Int = 0,
    @ColumnInfo("create_time")
    val createTime: Long = System.currentTimeMillis(),
    @ColumnInfo("update_time")
    val updateTime: Long = System.currentTimeMillis(),

    ) :
    Serializable