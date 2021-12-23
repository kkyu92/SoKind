package com.sokind.ui.guide.tabs.deep

import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FragmentGuideDeepBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideDeepFragment : BaseFragment<FragmentGuideDeepBinding>(R.layout.fragment_guide_deep) {
    private val viewModel by viewModels<GuideDeepViewModel>()

    override fun init() {

    }

    companion object {
        fun newInstance() = GuideDeepFragment()
    }

}