package com.smart.note

import android.app.Application
import com.smart.note.di.component.AppComponent
import com.smart.note.di.module.AppModule
import com.smart.note.di.component.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}