package com.sokind.ui.onboarding.screen

import android.content.Context
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.FragmentThirdBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ThirdScreen : BaseFragment<FragmentThirdBinding>(R.layout.fragment_third) {

    override fun init() {
        binding.apply {
            btStart
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_boardingFragment_to_bottomNavActivity)
                    onBoardingFinished()
                }, { it.printStackTrace() })
        }
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
    

    companion object {
        private const val TAG = "ThirdScreen"
    }
}