package com.smart.note

import android.app.Application
import com.smart.note.dagger2.AppComponent
import com.smart.note.dagger2.AppModule
import com.smart.note.dagger2.DaggerAppComponent

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