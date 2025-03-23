package com.smart.basic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    // 用于保存绑定对象，避免内存泄漏
    private var _binding: T? = null
    protected val binding: T
        get() = _binding ?: throw IllegalStateException("Binding accessed after onDestroyView")

    // 抽象方法，子类必须实现如何创建 Binding 对象
    protected abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): T
    open fun inject() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        _binding = initBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view, savedInstanceState)
        bindListener()
        bindFlow()
        initData(view, savedInstanceState)
    }

    protected abstract fun bindView(view: View, savedInstanceState: Bundle?)
    protected abstract fun bindListener()
    protected abstract fun bindFlow()
    protected open fun initData(view: View, savedInstanceState: Bundle?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}