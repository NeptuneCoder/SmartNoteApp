package com.smart.note.module.main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.basic.fragment.BaseFragment
import com.smart.note.App
import com.smart.note.R
import com.smart.note.data.Memo
import com.smart.note.databinding.FragmentHomeBinding
import com.smart.note.databinding.ItemCardViewBinding
import com.smart.note.ext.formatMillisToDateTime
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var homeViewModel: HomeViewModel
    private val data = mutableListOf<Memo>()
    private val adapter by lazy {
        ItemCardAdapter(data) { res ->
            //TODO 带着参数跳转到下一个界面
            val bundle = Bundle().apply {
                putInt("memo_id", res.id)
            }
            findNavController()
                .navigate(R.id.action_HomeFragment_to_DetailFragment, bundle)
        }
    }

    override fun inject() {
        super.inject()
        App.appComponent
            .homeComponent()
            .create()
            .inject(this)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }


    override fun bindView(view: View, savedInstanceState: Bundle?) {
        binding.recycleView.layoutManager = LinearLayoutManager(this.context).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recycleView.adapter = adapter
    }


    override fun bindListener() {
        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_HomeFragment_to_EditFragment)
        }
    }

    override fun bindFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.dataFlow
                    .collect {
                        data.clear()
                        data.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
            }
        }
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {
        super.initData(view, savedInstanceState)
        homeViewModel.requestData()
    }

    class ItemCardAdapter(
        private val data: MutableList<Memo>,
        private val itemClick: (Memo) -> Unit
    ) : RecyclerView.Adapter<ItemCardViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ItemCardViewHolder {
            return ItemCardViewHolder(
                ItemCardViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(
            holder: ItemCardViewHolder,
            position: Int
        ) {
            holder.bindData(data[position]) {
                itemClick.invoke(it)
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }

    class ItemCardViewHolder(private val binding: ItemCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(memo: Memo, itemClick: (Memo) -> Unit) {
            binding.contentTv.text = memo.content
            binding.timeTv.text = memo.createTime.formatMillisToDateTime()
            binding.root.setOnClickListener {
                itemClick.invoke(memo)
            }
        }
    }
    override fun onCreateToolbarMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onToolbarMenuItemClick(item: MenuItem): Boolean {
         return when (item.itemId) {
            R.id.action_settings -> {
                // 处理搜索逻辑
                findNavController().navigate(R.id.action_HomeFragment_to_SettingFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


