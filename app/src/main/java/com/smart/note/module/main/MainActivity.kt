package com.smart.note.module.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.smart.basic.fragment.ToolbarMenuHandler
import com.smart.note.App
import com.smart.note.R
import com.smart.note.databinding.ActivityMainBinding
import com.smart.note.module.detail.DetailFragment
import com.smart.note.module.edit.EditFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 初始化 Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        // 监听 Fragment 切换事件
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 当 Fragment 切换时，强制刷新菜单
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // 清空旧菜单（避免多个 Fragment 的菜单叠加）
        menu.clear()

        // 获取当前 Fragment
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                ?.childFragmentManager?.primaryNavigationFragment

        // 如果当前 Fragment 是 HomeFragment，不加载 Activity 的菜单，由 Fragment 自行处理
        if (currentFragment is ToolbarMenuHandler) {
            currentFragment.onCreateToolbarMenu(menu, menuInflater)
            return true
        }
        // 加载 Activity 的全局菜单（可选）
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                ?.childFragmentManager?.primaryNavigationFragment
        if (currentFragment is ToolbarMenuHandler) {
            if (currentFragment.onToolbarMenuItemClick(item)) {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}