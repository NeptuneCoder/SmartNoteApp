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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val app: Application) : BaseViewModel(app),
    DefaultLifecycleObserver {
    @Inject
    lateinit var memoDao: MemoDao

    private val _dataFlow = MutableStateFlow<Memo?>(null)
    val dataFlow = _dataFlow.asStateFlow()


    private val _aiSummaryFlow = MutableStateFlow<Pair<State, String>>(State.Default to "")
    val aiSummaryFlow = _aiSummaryFlow.asStateFlow()

    @Inject
    lateinit var chatRepository: ChatRepository

    init {
        App.appComponent
            .detailComponent()
            .create(app)
            .inject(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.i("chatRepository", "chatRepository ==== $chatRepository")

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

    fun aiSummery(id: Int) {
        viewModelScope.launch {
            val data = memoDao.getMemoById(id)
            if (data?.aiSummary?.isEmpty() == true) {
                chatRepository.sendMessage("帮我总结一下内容:${data.content}")
                    .onStart {
                        Log.i("chatRepository", "onStart")
                        _aiSummaryFlow.value = State.Loading to ""
                    }
                    .onCompletion {
                        Log.i("chatRepository", "onCompletion")
                    }
                    .catch {
                        Log.i("chatRepository", "catch")
                        _aiSummaryFlow.value = State.Error to ""
                    }
                    .collect {
                        Log.i("chatRepository", "chatRepository res ==== $it")
                        if (it.choices.isNotEmpty()) {
                            _aiSummaryFlow.value = State.Complete to it.choices[0].message.content
                        } else {
                            _aiSummaryFlow.value = State.Complete to "没有内容"
                        }
                    }


            } else {
                _aiSummaryFlow.value = State.Complete to ""
                _aiSummaryFlow.value = State.Complete to (data?.aiSummary ?: "没有内容")
            }


        }

    }

    fun collection(id: Int, call: () -> Unit) {
        val summary = _aiSummaryFlow.value.second
        if (summary.isNotEmpty()) {
            viewModelScope.launch {
                val data = memoDao.getMemoById(id)
                data?.aiSummary = summary
                data?.updateTime = System.currentTimeMillis()
                data?.let {
                    memoDao.update(data)
                }
                withContext(Dispatchers.Main) {
                    call.invoke()
                    requestData(id)
                }
            }
        }
    }
}