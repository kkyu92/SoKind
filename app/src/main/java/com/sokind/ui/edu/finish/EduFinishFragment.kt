package com.sokind.ui.edu.finish

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.FragmentEduFinishBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EduFinishFragment : BaseFragment<FragmentEduFinishBinding>(R.layout.fragment_edu_finish) {
    private val viewModel by viewModels<EduFinishViewModel>()

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    requireActivity().finish()
                }, { it.printStackTrace() })
            llGoReport
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent().putExtra(Constants.MOVE_TO, "report")
                    requireActivity().setResult(RESULT_OK, intent)
                    requireActivity().finish()
                }, { it.printStackTrace() })
            llGoRetry
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_eduFinishFragment_to_eduFragment)
                }, { it.printStackTrace() })
            llGoList
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent().putExtra(Constants.MOVE_TO, "cs")
                    requireActivity().setResult(RESULT_OK, intent)
                    requireActivity().finish()
                }, { it.printStackTrace() })
            cardView
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_eduFinishFragment_to_eduFragment)
                    // 인자값 넣어서 EduFragment 에서 api 호출
                }, { it.printStackTrace() })
        }
    }
}