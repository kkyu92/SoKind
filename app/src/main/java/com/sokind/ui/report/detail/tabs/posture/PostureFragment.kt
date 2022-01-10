package com.sokind.ui.report.detail.tabs.posture

import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FragmentPostureBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostureFragment : BaseFragment<FragmentPostureBinding>(R.layout.fragment_posture) {
    private val viewModel by viewModels<PostureViewModel>()

    override fun init() {

    }

    companion object {
        fun newInstance() = PostureFragment()
    }
}