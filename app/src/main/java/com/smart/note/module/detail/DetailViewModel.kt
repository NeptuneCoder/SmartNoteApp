package com.smart.note.module.detail

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.smart.basic.viewmodel.BaseViewModel
import com.smart.note.App
import com.smart.note.R
import com.smart.note.data.Memo
import com.smart.note.ext.md5
import com.smart.note.net.ApiService
import com.smart.note.room.MemoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(app: Application) : BaseViewModel(app),
    DefaultLifecycleObserver {
    @Inject
    lateinit var memoDao: MemoDao

    private val _dataFlow = MutableStateFlow<Memo?>(null)
    val dataFlow = _dataFlow.asStateFlow()

    init {
        App.appComponent
            .detailComponent()
            .create()
            .inject(this)
    }


    fun requestData(id: Int) {
        viewModelScope.launch {
            val res = memoDao.getMemoById(id)
            res?.let {
                _dataFlow.value = res
            }
        }
    }
}