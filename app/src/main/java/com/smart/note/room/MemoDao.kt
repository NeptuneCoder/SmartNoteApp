package com.smart.note.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.smart.note.data.Memo
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {
    @Insert
    suspend fun insert(memo: Memo)

    @Update
    suspend fun update(memo: Memo)


    @Delete
    suspend fun delete(memo: Memo)

    @Query("SELECT * FROM tab_memo ORDER BY create_time DESC")
    fun getAllMemos(): Flow<MutableList<Memo>> // 使用 Flow 实现实时数据监听

    @Query("SELECT * FROM tab_memo WHERE md5 = :md5")
    suspend fun getMemoById(md5: String): Memo?
}