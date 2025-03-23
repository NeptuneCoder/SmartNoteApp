package com.smart.note.module.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import com.smart.basic.viewmodel.BaseViewModel
import com.smart.note.App
import com.smart.note.R
import com.smart.note.data.Memo
import com.smart.note.ext.md5
import com.smart.note.net.ApiService
import com.smart.note.room.MemoDao
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditViewModel @Inject constructor(app: Application) : BaseViewModel(app),
    DefaultLifecycleObserver {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var memoDao: MemoDao

    init {
        App.appComponent
            .editComponent()
            .create()
            .inject(this)

    }

    fun save(content: String, successCall: suspend (Int) -> Unit,errorCall:suspend (Int) -> Unit) {
        viewModelScope.launch {
            val res = memoDao.getMemoById(content.md5())
            res?.let {
                errorCall.invoke(R.string.content_saved)
            } ?: suspend {
                memoDao.insert(Memo(content = content, md5 = content.md5()))
                successCall.invoke(R.string.save_success)
            }.invoke()


        }
    }
}