package com.smart.note.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.smart.basic.BaseFragment
import com.smart.note.R
import com.smart.note.databinding.FragmentEditBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditFragment : BaseFragment<FragmentEditBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            findNavController().navigate(R.id.action_EditFragment_to_HomeFragment)
        }
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentEditBinding {
        return FragmentEditBinding.inflate(inflater, container, false)
    }

}