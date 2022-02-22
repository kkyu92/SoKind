package com.sokind.ui.report.detail.tabs.speech2

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.report.ReportSpeak
import com.sokind.databinding.FragmentSpeech2Binding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.dialog.BottomSheetExplainDialog
import java.util.concurrent.TimeUnit

class Speech2Fragment(
    private val speakData: ReportSpeak,
    private val name: String
) : BaseFragment<FragmentSpeech2Binding>(R.layout.fragment_speech2) {
    private val viewModel by viewModels<Speech2ViewModel>()

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            var speech = ""

            tvComment.text = speakData.analysisMent
            tvComplain.text = speakData.complainScore

            pbSpeech.apply {
                avgKind.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    horizontalBias = speakData.sentimentScore.toFloat() / 100
                }
                myKind.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    horizontalBias = speakData.sentimentScore.toFloat() / 100
                }
                tvMin.text = getString(R.string.negative)
                tvMid.text = getString(R.string.neutrality)
                tvMax.text = getString(R.string.positive)
                tvIconName.text = getString(R.string.candidate)
                when(speakData.sentimentScore) {
                    in 0.0..0.33 -> {
                        speech = getString(R.string.negative)
                        avgPin.setImageDrawable(getDrawable(R.drawable.icon_negative))
                    }
                    in 0.33..0.66 -> {
                        speech = getString(R.string.neutrality)
                        avgPin.setImageDrawable(getDrawable(R.drawable.icon_neutral))
                    }
                    in 0.66..1.0 -> {
                        speech = getString(R.string.positive)
                        avgPin.setImageDrawable(getDrawable(R.drawable.icon_positive))
                    }
                }
            }
            tvTone.text = speakData.tone
            tvToneContent.text = getString(R.string.tone_content, name, speakData.tone)
            tvSpeechComment.text = getString(R.string.speech_kind_comment_1, name, speech)
            tvSpeedSps.text = speakData.speed.toString()

            tvComplainTitle
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetExplainDialog.newInstance(Constants.COMPLAIN_DIALOG, null)
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })

            tvSpeedTitle
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetExplainDialog.newInstance(Constants.SPEED_DIALOG, null)
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })
        }
    }
}