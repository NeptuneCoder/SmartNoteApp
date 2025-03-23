package com.smart.note.module.edit

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.smart.basic.fragment.BaseFragment
import com.smart.note.App
import com.smart.note.R
import com.smart.note.databinding.FragmentEditBinding
import com.smart.note.ext.lifecycleOnRepeat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditFragment : BaseFragment<FragmentEditBinding>() {

    @Inject
    lateinit var editViewModel: EditViewModel
    override fun inject() {
        App.appComponent
            .editComponent()
            .create()
            .inject(this)
        lifecycle.addObserver(editViewModel)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentEditBinding {
        return FragmentEditBinding.inflate(inflater, container, false)
    }

    override fun bindView(view: View, savedInstanceState: Bundle?) {

    }

    override fun bindListener() {
        binding.saveButton.setOnClickListener {
            val res = binding.contentEt.text.toString()
            if (res.isEmpty()) {
                Toast.makeText(this.context, R.string.content_is_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                editViewModel.save(content = res.toString(), {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditFragment.context, it, Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }, {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditFragment.context, it, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun bindFlow() {
        lifecycleOnRepeat {
            editViewModel.dataFlow
                .collect {
                    it?.let {
                        binding.contentEt.setText(it.content)
                    }
                }
        }
    }

    private val memoId by lazy { arguments?.getInt("memo_id") ?: -1 }
    override fun initData(view: View, savedInstanceState: Bundle?) {
        super.initData(view, savedInstanceState)
        Log.i("memoId", "memoId === $memoId")
        if (memoId != -1) {
            editViewModel.requestData(memoId)
        }
    }

    override fun onCreateToolbarMenu(menu: Menu, inflater: MenuInflater) {

    }

    override fun onToolbarMenuItemClick(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}