package com.smart.note.di.module

import com.smart.note.di.component.ChatComponent
import com.smart.note.di.component.DetailComponent
import com.smart.note.di.component.EditComponent
import com.smart.note.di.component.HomeComponent
import com.smart.note.di.component.MainComponent
import dagger.Module

//3. 这个是链接子组件和主组件的桥梁
@Module(subcomponents = [MainComponent::class, EditComponent::class, HomeComponent::class, DetailComponent::class, ChatComponent::class])
class SubComponentModule