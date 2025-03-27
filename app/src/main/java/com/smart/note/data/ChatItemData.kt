package com.smart.note.data

import java.io.Serializable

data class ChatItemData(
    val netState: NetState = NetState.Default,
    val chatData: MutableList<ChatData> = mutableListOf<ChatData>(),
    var time: Long = System.currentTimeMillis()
) : Serializable