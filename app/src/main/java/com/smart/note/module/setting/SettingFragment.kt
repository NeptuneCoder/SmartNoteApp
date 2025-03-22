package com.smart.note.module.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.smart.basic.BaseFragment
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SettingFragment_to_HomeFragment)
        }
    }


}