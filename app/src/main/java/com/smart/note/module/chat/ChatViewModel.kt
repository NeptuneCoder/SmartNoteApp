package com.smart.note.module.chat

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

class ChatViewModel @Inject constructor(app: Application) : BaseViewModel(app),
    DefaultLifecycleObserver {
    @Inject
    lateinit var memoDao: MemoDao


    @Inject
    lateinit var chatRepository: ChatRepository

    init {
        App.appComponent
            .chatComponent()
            .create()
            .inject(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i("chatRepository", "chatRepository ==== $chatRepository")

    }

}