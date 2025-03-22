package com.smart.note.dagger2.edit

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