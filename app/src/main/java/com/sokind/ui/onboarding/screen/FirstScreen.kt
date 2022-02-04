package com.sokind.ui.onboarding.screen

import com.sokind.R
import com.sokind.databinding.FragmentFirstBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstScreen : BaseFragment<FragmentFirstBinding>(R.layout.fragment_first) {

    override fun init() {
        binding.tvBoardingTitle.text = fromHtml(getString(R.string.boarding_title, userName))
    }

    companion object {
        private const val TAG = "FirstScreen"
    }
}