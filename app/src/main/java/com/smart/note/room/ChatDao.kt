package com.smart.note.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smart.note.data.ChatDetail
import com.smart.note.data.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert
    suspend fun insert(chatDetail: ChatDetail)

    @Update
    suspend fun update(chatDetail: ChatDetail)


    @Delete
    suspend fun delete(chatDetail: ChatDetail)

    @Query("DELETE FROM tab_chat_detail WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM tab_chat_detail ORDER BY create_time DESC")
    fun getAllChatDetail(): Flow<MutableList<ChatDetail>> // 使用 Flow 实现实时数据监听

    @Query("SELECT * FROM tab_chat_detail WHERE id = :id")
    suspend fun getChatDetailById(id: Int): ChatDetail?
}