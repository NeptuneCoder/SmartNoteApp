package com.smart.note.dagger2

import com.smart.note.dagger2.edit.EditComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, SubComponentModule::class])
interface AppComponent {
    fun editComponent(): EditComponent.Factory
}