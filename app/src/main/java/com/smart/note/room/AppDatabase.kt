package com.smart.note.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smart.note.data.ChatDetail
import com.smart.note.data.Memo
import com.smart.note.data.Topic


@Database(
    entities = [Memo::class, ChatDetail::class, Topic::class],
    version = 4,
    exportSchema = true,// 关闭 Schema 导出（生产环境建议开启）
//    autoMigrations = [AutoMigration(from = 2, to = 3)] //新增表式不需要手动的写Migration
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 执行 SQL 添加新列
                database.execSQL(
                    "ALTER TABLE tab_memo ADD COLUMN ai_summary TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        // 版本 1 → 2：创建 tab_chat_detail 表
        val CHAT_MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `tab_chat_detail` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `content` TEXT NOT NULL,
                `type` INTEGER NOT NULL,
                `topic_id` TEXT NOT NULL DEFAULT '',
                `topic_name` TEXT NOT NULL DEFAULT '',
                `create_time` INTEGER NOT NULL DEFAULT 0
            )
        """
                )
            }
        }

        // 版本 2 → 3：创建 tab_topic 表
        val TOPIC_MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `tab_topic` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `topic_id` TEXT NOT NULL DEFAULT '',
                `topic_name` TEXT NOT NULL DEFAULT '',
                `create_time` INTEGER NOT NULL DEFAULT 0
            )
        """
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_note_db" // 数据库文件名
                ).addMigrations(MIGRATION_1_2, CHAT_MIGRATION_3_4, TOPIC_MIGRATION_3_4)

                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}