package com.smart.note.di.module

import android.app.Application
import com.smart.note.di.scope.RoomScope
import com.smart.note.room.AppDatabase
import com.smart.note.room.ChatDao
import com.smart.note.room.MemoDao
import dagger.Module
import dagger.Provides

@Module
class RoomModule {
    @RoomScope
    @Provides
    fun provideMemoDao(appDatabase: AppDatabase): MemoDao {
        return appDatabase.memoDao()
    }

    @RoomScope
    @Provides
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }

    @RoomScope
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return AppDatabase.getDatabase(app)
    }
}