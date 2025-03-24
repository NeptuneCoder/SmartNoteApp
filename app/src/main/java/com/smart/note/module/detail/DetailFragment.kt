package com.smart.note.module.detail

import android.os.Bundle
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smart.basic.fragment.BaseFragment
import com.smart.note.App
import com.smart.note.R
import com.smart.note.data.NetState
import com.smart.note.databinding.FragmentDetailBinding
import com.smart.note.ext.lifecycleOnRepeat
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    @Inject
    lateinit var detailViewModel: DetailViewModel

    @Inject
    lateinit var loadAiSummaryDialog: AlertDialog


    override fun inject() {
        super.inject()
        App.appComponent
            .detailComponent()
            .create(requireActivity())
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
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("删除")
                    .setMessage("确定要执行删除操作吗？")
                    .setPositiveButton("删除") { _, _ ->
                        // 确定按钮点击事件
                        detailViewModel.delete(memoId) {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                    }
                    .setNegativeButton("取消", null)
                    .show()
                true
            }

            R.id.action_ai -> {
                detailViewModel.aiSummery(memoId)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun bindListener() {
        Log.i("bindListener", "context === $context")
        binding.fab.setOnClickListener {
            //TODO 带着参数跳转到下一个界面
            val bundle = Bundle().apply {
                putInt("memo_id", memoId)
            }
            findNavController()
                .navigate(R.id.action_DetailFragment_to_EditFragment, bundle)
        }
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            // 判断是否滑动到底部
            val isAtBottom = v.getChildAt(0)?.let { child ->
                v.height + scrollY >= child.height
            } == true

            if (isAtBottom) {
                binding.fab.hide()
            } else {
                binding.fab.show()
            }
        }
    }


    override fun bindFlow() {
        lifecycleOnRepeat {
            detailViewModel.dataFlow.collect {
                it?.let {
                    binding.contentDetailTv.text = buildString {
                        append(it.content)
                        append("\n")
                        if (it.aiSummary.isNotEmpty()) {
                            append("deepSeek总结：\n")
                            append(it.aiSummary)
                        }
                    }
                }
            }
        }
        lifecycleOnRepeat {
            detailViewModel.aiSummaryFlow.collect {
                if (it.first == NetState.Loading) {
                    loadAiSummaryDialog.show()
                } else if (it.first == NetState.Complete) {
                    loadAiSummaryDialog.dismiss()
                    if (it.second.isNotEmpty()) {
                        MaterialAlertDialogBuilder(requireActivity())
                            .setTitle("总结内容")
                            .setMessage(it.second)
                            .setPositiveButton("收藏") { _, _ ->
                                // 确定按钮点击事件
                                detailViewModel.collection(memoId) {
                                    Toast.makeText(
                                        this@DetailFragment.context, R.string.collection_success,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                } else if (it.first == NetState.Error) {
                    loadAiSummaryDialog.dismiss()
                    Toast.makeText(requireActivity(), "加载失败", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private val memoId by lazy { arguments?.getInt("memo_id") ?: -1 }
    override fun initData(view: View, savedInstanceState: Bundle?) {
        super.initData(view, savedInstanceState)
        lifecycle.addObserver(detailViewModel)
        detailViewModel.requestData(memoId)

    }
}



