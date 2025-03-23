package com.smart.note.di.module

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
    fun provideAlertDialog(context: Context): AlertDialog {
        return MaterialAlertDialogBuilder(context)
            .setTitle("加载中...")        // 设置标题
            .setMessage("请稍候")         // 可选提示文字
            .setCancelable(false)      // 禁止点击外部取消
            .create()
    }
}