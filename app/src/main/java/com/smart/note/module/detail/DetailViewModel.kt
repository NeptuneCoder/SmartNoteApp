package com.smart.note.module.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.smart.basic.viewmodel.BaseViewModel
import com.smart.note.App
import com.smart.note.R
import com.smart.note.data.Memo
import com.smart.note.ext.md5
import com.smart.note.net.ApiService
import com.smart.note.repository.ChatRepository
import com.smart.note.room.MemoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class DetailViewModel @Inject constructor(app: Application) : BaseViewModel(app),
    DefaultLifecycleObserver {
    @Inject
    lateinit var memoDao: MemoDao

    private val _dataFlow = MutableStateFlow<Memo?>(null)
    val dataFlow = _dataFlow.asStateFlow()

    @Inject
    lateinit var chatRepository: ChatRepository

    init {
        App.appComponent
            .detailComponent()
            .create()
            .inject(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i("chatRepository", "chatRepository ==== $chatRepository")
        viewModelScope.launch {
            val  res = chatRepository.sendMessage("你好呀")
            Log.i("chatRepository", "chatRepository res ==== $res")
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

    fun delete(id: Int, call: (Int) -> Unit) {
        viewModelScope.launch {
            memoDao.deleteById(id)
            withContext(Dispatchers.Main) {
                call.invoke(R.string.delete_success)
            }
        }
    }
}