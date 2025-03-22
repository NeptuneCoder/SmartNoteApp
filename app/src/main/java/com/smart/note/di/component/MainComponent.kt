package com.smart.note.di.component

import com.smart.note.di.module.MainModule
import com.smart.note.di.scope.MainScope
import com.smart.note.module.main.MainActivity
import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(mainActivity: MainActivity)

}