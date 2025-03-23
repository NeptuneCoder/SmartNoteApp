package com.smart.note.module.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.smart.basic.viewmodel.BaseViewModel
import com.smart.note.App
import com.smart.note.data.Memo
import com.smart.note.room.MemoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(app: Application) : BaseViewModel(app) {
    @Inject
    lateinit var memoDao: MemoDao
    private val _dataFlow = MutableStateFlow<MutableList<Memo>>(mutableListOf())
    val dataFlow = _dataFlow.asStateFlow()

    init {
        App.appComponent
            .homeComponent()
            .create()
            .inject(this)
    }

    fun requestData() {
        Log.i("memoDao", "memoDao === $memoDao")
        viewModelScope
            .launch {
                memoDao.getAllMemos()
                    .onStart { }
                    .onCompletion { }
                    .collect {
                        _dataFlow.value = it
                    }
            }
    }
}