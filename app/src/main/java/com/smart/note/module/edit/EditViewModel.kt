package com.smart.note.module.edit

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

class EditViewModel @Inject constructor(app: Application) : BaseViewModel(app),
    DefaultLifecycleObserver {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var memoDao: MemoDao

    private val _dataFlow = MutableStateFlow<Memo?>(null)
    val dataFlow = _dataFlow.asStateFlow()

    init {
        App.appComponent
            .editComponent()
            .create()
            .inject(this)

    }

    fun save(
        content: String,
        successCall: suspend (Int) -> Unit,
        errorCall: suspend (Int) -> Unit
    ) {
        viewModelScope.launch {
            if (_dataFlow.value == null) {
                memoDao.insert(Memo(content = content, md5 = content.md5()))
                successCall.invoke(R.string.save_success)
            } else {
                val memo = _dataFlow.value
                memo?.let {
                    it.content = content
                    it.md5 = content.md5()
                    it.updateTime = System.currentTimeMillis()
                    memoDao.update(it)
                    successCall.invoke(R.string.update_success)
                }
            }
        }
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