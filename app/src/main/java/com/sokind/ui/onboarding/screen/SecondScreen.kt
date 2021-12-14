package com.sokind.ui.onboarding.screen

import com.sokind.R
import com.sokind.databinding.FragmentSecondBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondScreen : BaseFragment<FragmentSecondBinding>(R.layout.fragment_second) {

    override fun init() {

    }

    companion object {
        private const val TAG = "SecondScreen"
    }
}