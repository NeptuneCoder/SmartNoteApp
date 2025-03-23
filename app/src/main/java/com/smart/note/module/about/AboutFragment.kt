package com.smart.note.module.about

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
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smart.basic.fragment.BaseFragment
import com.smart.note.App
import com.smart.note.R
import com.smart.note.databinding.FragmentAboutBinding
import com.smart.note.databinding.FragmentDetailBinding
import com.smart.note.ext.lifecycleOnRepeat
import com.smart.note.module.detail.DetailViewModel
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    override fun inject() {
        super.inject()

    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }


    override fun bindView(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
    }

    override fun onCreateToolbarMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onToolbarMenuItemClick(item: MenuItem): Boolean {
        Log.i("onToolbarMenuItemClick", "onToolbarMenuItemClick === $item")
        return when (item.itemId) {
            R.id.action_delete -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun bindListener() {

    }

    override fun bindFlow() {

    }
    
    override fun initData(view: View, savedInstanceState: Bundle?) {
        super.initData(view, savedInstanceState)

    }
}



