package com.smart.note.di.module

import android.app.Application
import com.smart.note.App
import dagger.Module
import dagger.Provides


@Module
class AppModule(val app: App) {

    @Provides
    fun provideApp(): Application {
        return app
    }

}