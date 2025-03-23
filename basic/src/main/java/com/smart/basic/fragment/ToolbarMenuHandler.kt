package com.smart.basic.fragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

// 定义接口
interface ToolbarMenuHandler {
    fun onCreateToolbarMenu(menu: Menu, inflater: MenuInflater)
    fun onToolbarMenuItemClick(item: MenuItem): Boolean
}