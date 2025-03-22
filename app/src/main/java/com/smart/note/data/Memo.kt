package com.smart.note.data

import java.io.Serializable

data class Memo(val id: Int, val content: String, val createTime: Long, val updateTime: Long) :
    Serializable