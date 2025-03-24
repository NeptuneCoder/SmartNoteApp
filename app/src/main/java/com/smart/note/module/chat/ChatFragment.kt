package com.smart.note.module.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smart.basic.fragment.BaseFragment
import com.smart.note.App
import com.smart.note.R
import com.smart.note.data.NetState
import com.smart.note.databinding.FragmentChatBinding
import com.smart.note.databinding.FragmentDetailBinding
import com.smart.note.databinding.ItemCardViewBinding
import com.smart.note.databinding.ItemChatViewBinding
import com.smart.note.ext.formatMillisToDateTime
import com.smart.note.ext.lifecycleOnRepeat
import com.smart.note.model.ChatContent
import com.smart.note.module.detail.DetailViewModel
import com.smart.note.module.main.HomeFragment.ItemCardViewHolder
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ChatFragment : BaseFragment<FragmentChatBinding>() {

    @Inject
    lateinit var chatViewModel: ChatViewModel

    @Inject
    lateinit var loadAiSummaryDialog: AlertDialog

    private val data = mutableListOf<ChatContent>()
    private val adapter by lazy { ChatAdapter(data) }
    override fun inject() {
        super.inject()
        App.appComponent
            .chatComponent()
            .create(requireActivity())
            .inject(this)
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }


    override fun bindView(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        binding.recycleView.layoutManager = LinearLayoutManager(requireActivity()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recycleView.adapter = adapter
    }

    private class ChatAdapter(val data: MutableList<ChatContent>) :
        RecyclerView.Adapter<ChatViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ChatViewHolder {
            return ChatViewHolder(
                ItemChatViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(
            holder: ChatViewHolder,
            position: Int
        ) {
            holder.bindData(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

    class ChatViewHolder(private val binding: ItemChatViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: ChatContent) {
            binding.contentTv.text = data.content
            binding.timeTv.text = data.createTime.formatMillisToDateTime()
            binding.aiTv.text = if (data.type == 0) "Sf" else "AI"
        }

    }

    override fun onCreateToolbarMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onToolbarMenuItemClick(item: MenuItem): Boolean {
        Log.i("onToolbarMenuItemClick", "onToolbarMenuItemClick === $item")
        return when (item.itemId) {
            R.id.action_delete -> {

                true
            }

            R.id.action_ai -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun bindListener() {
        binding.contentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.sendBtn.isEnabled = s?.isNotEmpty() == true
            }

        })
        binding.sendBtn.setOnClickListener {
            val content = binding.contentEt.text
            chatViewModel.chat(content.toString())
        }
    }

    override fun bindFlow() {
        lifecycleOnRepeat {
            chatViewModel.chatContentFlow
                .collect {
                    if (it.first == NetState.Start) {
                        binding.contentEt.setText("")
                        data.clear()
                        data.addAll(it.second)
                        adapter.notifyDataSetChanged()
                    } else if (it.first == NetState.Loading) {
//                        loadAiSummaryDialog.show()
                        data.clear()
                        data.addAll(it.second)
                        adapter.notifyDataSetChanged()
                    } else if (it.first == NetState.Complete) {
//                        loadAiSummaryDialog.dismiss()
                        data.clear()
                        data.addAll(it.second)
                        adapter.notifyDataSetChanged()
                    }
                }
        }

    }

    private val memoId by lazy { arguments?.getInt("memo_id") ?: -1 }
    override fun initData(view: View, savedInstanceState: Bundle?) {
        super.initData(view, savedInstanceState)
        lifecycle.addObserver(chatViewModel)
    }
}



