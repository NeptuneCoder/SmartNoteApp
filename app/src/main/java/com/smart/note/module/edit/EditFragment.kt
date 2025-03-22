package com.smart.note.module.edit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.smart.basic.BaseFragment
import com.smart.note.App
import com.smart.note.R
import com.smart.note.data.Memo
import com.smart.note.databinding.FragmentEditBinding
import com.smart.note.net.ApiService
import com.smart.note.room.MemoDao
import kotlinx.coroutines.launch
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
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentEditBinding {
        return FragmentEditBinding.inflate(inflater, container, false)
    }

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var memoDao: MemoDao
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("apiService", "apiService === $apiService")
        Log.i("apiService", "memoDao === $memoDao")
        Log.i("apiService", "editViewModel === $editViewModel")

        binding.saveButton.setOnClickListener {
            lifecycleScope.launch {
                memoDao.insert(Memo(content = "测试内容", md5 = ""))
            }
        }
    }


}