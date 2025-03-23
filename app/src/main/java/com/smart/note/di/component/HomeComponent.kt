package com.smart.note.di.component

import com.smart.note.di.module.MainModule
import com.smart.note.di.scope.MainScope
import com.smart.note.module.main.HomeFragment
import com.smart.note.module.main.HomeViewModel
import com.smart.note.module.main.MainActivity
import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [])
interface HomeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(homeFragment: HomeFragment)
    fun inject(homeViewModel: HomeViewModel)

}