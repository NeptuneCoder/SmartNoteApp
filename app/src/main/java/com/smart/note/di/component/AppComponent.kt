package com.smart.note.di.component

import com.smart.note.di.module.AppModule
import com.smart.note.di.module.NetModule
import com.smart.note.di.module.RoomModule
import com.smart.note.di.module.SubComponentModule
import com.smart.note.di.module.ViewModelModule
import com.smart.note.di.scope.NetScope
import com.smart.note.di.scope.RoomScope
import com.smart.note.di.scope.ViewModelScope
import dagger.Component
import javax.inject.Singleton

@ViewModelScope
@NetScope
@RoomScope
@Component(modules = [AppModule::class, NetModule::class, RoomModule::class, ViewModelModule::class, SubComponentModule::class])
interface AppComponent {
    fun editComponent(): EditComponent.Factory
    fun mainComponent(): MainComponent.Factory
    fun homeComponent(): HomeComponent.Factory
    fun detailComponent(): DetailComponent.Factory
    fun chatComponent(): ChatComponent.Factory
}