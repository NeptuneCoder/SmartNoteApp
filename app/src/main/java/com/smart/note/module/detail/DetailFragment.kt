package com.smart.note.module.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.smart.basic.fragment.BaseFragment
import com.smart.note.App
import com.smart.note.R
import com.smart.note.databinding.FragmentDetailBinding
import com.smart.note.ext.lifecycleOnRepeat
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    @Inject
    lateinit var detailViewModel: DetailViewModel
    override fun inject() {
        super.inject()
        App.appComponent
            .detailComponent()
            .create()
            .inject(this)
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }


    override fun bindView(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
    }

    override fun onCreateToolbarMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onToolbarMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                // 处理搜索逻辑
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun bindListener() {
        binding.fab.setOnClickListener {
            //TODO 带着参数跳转到下一个界面
            val bundle = Bundle().apply {
                putInt("memo_id", memoId)
            }
            findNavController()
                .navigate(R.id.action_DetailFragment_to_EditFragment, bundle)
        }
    }

    override fun bindFlow() {
        lifecycleOnRepeat {
            detailViewModel.dataFlow.collect {
                it?.let {
                    binding.contentDetailTv.text = it.content
                }
            }
        }
    }

    private val memoId by lazy { arguments?.getInt("memo_id") ?: -1 }
    override fun initData(view: View, savedInstanceState: Bundle?) {
        super.initData(view, savedInstanceState)
        detailViewModel.requestData(memoId)
    }
}


