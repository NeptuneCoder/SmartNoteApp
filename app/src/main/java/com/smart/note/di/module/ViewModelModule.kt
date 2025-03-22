package com.smart.note.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.smart.note.di.factory.DaggerViewModelFactory
import com.smart.note.di.scope.ViewModelScope
import com.smart.note.module.edit.EditViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @ViewModelScope
    @Binds
    @IntoMap
    @ViewModelKey(EditViewModel::class)
    abstract fun bindMyViewModel(viewModel: EditViewModel): ViewModel

    @ViewModelScope
    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)