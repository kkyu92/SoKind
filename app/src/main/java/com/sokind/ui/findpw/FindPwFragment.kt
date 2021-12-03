package com.sokind.ui.findpw

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FindIdFragmentBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.findid.FindIdViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPwFragment : BaseFragment<FindIdFragmentBinding>(R.layout.find_pw_fragment) {
    private val viewModel by viewModels<FindIdViewModel>()

    override fun init() {

    }
}