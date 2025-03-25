package com.smart.note.module.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.smart.basic.viewmodel.BaseViewModel
import com.smart.note.App
import com.smart.note.data.NetState
import com.smart.note.data.ChatDetail
import com.smart.note.model.ChatRequest
import com.smart.note.model.Message
import com.smart.note.repository.ChatRepository
import com.smart.note.room.MemoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

class ChatViewModel @Inject constructor(app: Application) : BaseViewModel(app),
    DefaultLifecycleObserver {
    @Inject
    lateinit var memoDao: MemoDao

    @Inject
    lateinit var chatRepository: ChatRepository
    private val chatList = mutableListOf<ChatDetail>()

    private val _chatContentFLow =
        MutableStateFlow<Pair<NetState, MutableList<ChatDetail>>>(NetState.Default to chatList)
    val chatContentFlow = _chatContentFLow.asStateFlow()

    init {
        App.appComponent
            .chatComponent()
            .create(app)
            .inject(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i("chatRepository", "chatRepository ==== $chatRepository")

    }


    fun chat(content: String) {

        viewModelScope.launch {
            chatRepository.sendMessage(content)
                .catch {
                    _chatContentFLow.value = NetState.Complete to chatList.apply {
                        add(ChatDetail(type = 1, content = " 加载失败"))
                    }
                }
                .onStart {
                    _chatContentFLow.value =
                        (NetState.Start to chatList.apply {
                            add(ChatDetail(type = 0, content = content))
                        })

                }
                .onCompletion {

                }
                .collect {
                    if (it.choices.isNotEmpty()) {
                        _chatContentFLow.value = NetState.Complete to chatList.apply {
                            add(ChatDetail(type = 1, content = it.choices[0].message.content))
                        }
                    } else {
                        _chatContentFLow.value = NetState.Complete to chatList.apply {
                            add(ChatDetail(type = 1, content = " 加载失败"))
                        }
                    }

                }
        }


    }

    fun chatStream(content: String) {
        // 在 ViewModel 或 Presenter 中

        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.chatStream(content)
                .onStart {
                    _chatContentFLow.value =
                        (NetState.Start to chatList.apply {
                            add(ChatDetail(type = 0, content = content))
                        })
                }
                .catch {
                    _chatContentFLow.value = NetState.Complete to chatList.apply {
                        add(ChatDetail(type = 1, content = " 加载失败"))
                    }
                }
                .collect {
                    Log.i("chatStream", "chatStream === " + it)
                }
        }


    }

}