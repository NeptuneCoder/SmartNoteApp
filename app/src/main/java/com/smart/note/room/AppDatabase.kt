package com.smart.note.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smart.note.data.Memo


@Database(
    entities = [Memo::class],
    version = 1,
    exportSchema = false // 关闭 Schema 导出（生产环境建议开启）
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): MemoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_database" // 数据库文件名
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}