package com.smart.note.di.module

import com.smart.note.di.scope.EditScope
import dagger.Module
import dagger.Provides

@Module
class EditModule {

    @EditScope
    @Provides
    fun provideObject(): Any {
        return Any()
    }
}