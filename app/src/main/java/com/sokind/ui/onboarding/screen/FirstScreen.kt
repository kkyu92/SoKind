package com.sokind.ui.onboarding.screen

import android.text.Html
import com.sokind.R
import com.sokind.databinding.FragmentFirstBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstScreen : BaseFragment<FragmentFirstBinding>(R.layout.fragment_first) {

    override fun init() {
        binding.tvBoardingTitle.text = Html.fromHtml(String.format(getString(R.string.boarding_title, "asdf")))

    }

    companion object {
        private const val TAG = "FirstScreen"
    }
}