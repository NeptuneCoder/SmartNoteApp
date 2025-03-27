package com.smart.note.module.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.smart.basic.viewmodel.BaseViewModel
import com.smart.note.App
import com.smart.note.data.ChatData
import com.smart.note.data.NetState
import com.smart.note.data.ChatDetail
import com.smart.note.data.Type
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
    private val chatList = mutableListOf<ChatData>()

    private val _chatContentFLow =
        MutableStateFlow<Pair<NetState, Pair<Long, MutableList<ChatData>>>>(NetState.Default to (System.currentTimeMillis() to chatList))
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
                    _chatContentFLow.value =
                        NetState.Complete to (System.currentTimeMillis() to chatList.apply {
                            add(ChatData(type = 1, content = " 加载失败"))
                        })
                }
                .onStart {
                    _chatContentFLow.value =
                        (NetState.Start to (System.currentTimeMillis() to chatList.apply {
                            add(ChatData(type = 0, content = content))
                            add(
                                ChatData(
                                    type = 1,
                                    netState = NetState.Loading,
                                    content = "加载中，请稍后..."
                                )
                            )
                        }))

                }
                .onCompletion {

                }
                .collect {
                    if (it.choices.isNotEmpty()) {
                        chatList.removeAt(chatList.lastIndex)
                        val res = ChatData(
                            type = 1,
                            netState = NetState.Complete,
                            content = it.choices[0].message.content
                        )
                        chatList.add(res)
                        _chatContentFLow.value =
                            NetState.Complete to (System.currentTimeMillis() to chatList)
                    } else {
                        _chatContentFLow.value =
                            NetState.Complete to (System.currentTimeMillis() to chatList.apply {
                                add(ChatData(type = 1, content = " 加载失败"))
                            })
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
                        (NetState.Start to (System.currentTimeMillis() to chatList.apply {
                            add(ChatData(type = 0, chat = mutableListOf<String>().apply {
                                add(content)
                            }))
                            add(ChatData(type = 1, chat = mutableListOf<String>()))
                        }))
                }
                .catch {
                    _chatContentFLow.value =
                        NetState.Complete to (System.currentTimeMillis() to chatList.apply {
                            add(ChatData(type = 1, chat = mutableListOf<String>().apply {
                                add(content)
                            }))
                        })
                }
                .collect {
                    val findLast = chatList.findLast { it.type == 1 }
                    findLast?.chat?.add(it)
                    val sb = StringBuilder()
                    findLast?.chat?.forEach {
                        sb.append(it)
                    }
                    Log.i("chatStream", "chatStream sb  === " + sb)
                    _chatContentFLow.value =
                        NetState.Complete to (System.currentTimeMillis() to chatList)
                    Log.i("chatStream", "chatStream === " + it)
                }
        }


    }

}