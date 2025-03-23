package com.smart.note.module.setting

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
import com.smart.note.R

import com.smart.note.databinding.FragmentSettingBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }

    override fun bindView(view: View, savedInstanceState: Bundle?) {

    }

    override fun bindListener() {
        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SettingFragment_to_HomeFragment)
        }
    }

    override fun bindFlow() {
        
    }

    override fun onCreateToolbarMenu(menu: Menu, inflater: MenuInflater) {

    }

    override fun onToolbarMenuItemClick(item: MenuItem): Boolean {
        return true
    }

}