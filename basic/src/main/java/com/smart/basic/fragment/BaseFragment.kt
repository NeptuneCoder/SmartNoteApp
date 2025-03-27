package com.smart.basic.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding> : Fragment(), ToolbarMenuHandler {

    // 用于保存绑定对象，避免内存泄漏
    private var _binding: T? = null
    protected val binding: T
        get() = _binding ?: throw IllegalStateException("Binding accessed after onDestroyView")
    private var keyboardVisible = false
    private var rootView: View? = null

    // 子类必须返回布局的根视图
    fun getRootView(): View {
        return binding.root
    }

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
        rootView = getRootView()
        bindView(view, savedInstanceState)
        bindListener()
        bindFlow()
        initData(view, savedInstanceState)
        setupKeyboardListener()
    }

    private fun setupKeyboardListener() {
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView?.getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView?.height ?: 0
            val keypadHeight = screenHeight - rect.bottom

            // 判断键盘状态（阈值建议设为屏幕高度的 15%）
            val isKeyboardOpen = keypadHeight > screenHeight * 0.15

            if (isKeyboardOpen != keyboardVisible) {
                keyboardVisible = isKeyboardOpen
                if (isKeyboardOpen) {
                    onKeyboardOpened(keypadHeight)
                } else {
                    onKeyboardClosed()
                }
            }
        }
    }

    // 键盘弹起回调（子类可重写）
    open fun onKeyboardOpened(keyboardHeight: Int) {}

    // 键盘收起回调（子类可重写）
    open fun onKeyboardClosed() {}


    protected abstract fun bindView(view: View, savedInstanceState: Bundle?)
    protected abstract fun bindListener()
    protected abstract fun bindFlow()
    protected open fun initData(view: View, savedInstanceState: Bundle?) {

    }

    override fun onDestroyView() {
        rootView?.viewTreeObserver?.removeOnGlobalLayoutListener { /* 移除监听 */ }
        super.onDestroyView()
        _binding = null
    }
}