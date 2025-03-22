package com.smart.note.dagger2

import com.smart.note.dagger2.edit.EditComponent
import dagger.Module

@Module(subcomponents = [EditComponent::class])
class SubComponentModule {
}