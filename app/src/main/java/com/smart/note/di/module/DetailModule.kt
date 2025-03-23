package com.smart.note.di.module

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smart.note.di.scope.DetailScope
import dagger.Module
import dagger.Provides

@Module
class DetailModule() {
    @DetailScope
    @Provides
    fun provideContext(context: Context): Context {
        return context
    }
}