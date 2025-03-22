package com.smart.note.di.module

import com.smart.note.di.scope.EditScope
import com.smart.note.di.scope.MainScope
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideObject(): Any {
        return Any()
    }
}