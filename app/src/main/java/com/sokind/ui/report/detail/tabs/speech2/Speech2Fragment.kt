package com.sokind.ui.report.detail.tabs.speech2

import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FragmentSpeech2Binding
import com.sokind.ui.base.BaseFragment

class Speech2Fragment : BaseFragment<FragmentSpeech2Binding>(R.layout.fragment_speech2) {
    private val viewModel by viewModels<Speech2ViewModel>()

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            pbSpeech.apply {
                tvMin.text = getString(R.string.negative)
                tvMid.text = getString(R.string.neutrality)
                tvMax.text = getString(R.string.positive)
                tvIconName.text = getString(R.string.candidate)
            }

        }
    }

    companion object {
        fun newInstance() = Speech2Fragment()
    }

}