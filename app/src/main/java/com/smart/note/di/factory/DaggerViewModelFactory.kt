package com.smart.note.di.factory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class DaggerViewModelFactory @Inject constructor(
    private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.i("DaggerViewModelFactory", "DaggerViewModelFactory constructor")
        val creator = viewModelsMap[modelClass]
            ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
        return creator.get() as T
    }
}