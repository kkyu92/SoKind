package com.sokind.util.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.*
import com.sokind.util.Constants
import java.util.concurrent.TimeUnit

class BottomSheetExplainDialog(
    private val dialogTag: String,
    private val content: String?,
) : BottomSheetDialogFragment() {
    private lateinit var emotionBinding: DialogExplainEmotionBinding
    private lateinit var lvBinding: DialogExplainLvBinding
    private lateinit var kindBinding: DialogExplainKindBinding
    private lateinit var analysisBinding: DialogExplainAnalysisBinding
    private lateinit var complainBinding: DialogExplainComplainBinding
    private lateinit var speedBinding: DialogExplainSpeechBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        when(dialogTag) {
            Constants.EMOTION_DIALOG -> return emotionDialog(inflater, container)
            Constants.LV_DIALOG -> return lvDialog(inflater, container)
            Constants.KIND_DIALOG -> return kindDialog(inflater, container)
            Constants.ANALYSIS_DIALOG -> return analysisDialog(inflater, container)
            Constants.COMPLAIN_DIALOG -> return complainDialog(inflater, container)
            Constants.SPEED_DIALOG -> return speechDialog(inflater, container)
        }

        return emotionDialog(inflater, container)
    }

    private fun speechDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        speedBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_explain_speech, container, false)

        speedBinding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return speedBinding.root
    }

    private fun complainDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        complainBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_explain_complain, container, false)

        complainBinding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return complainBinding.root
    }

    private fun analysisDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        analysisBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_explain_analysis, container, false)

        analysisBinding.tvContent.text = getString(R.string.analysis_content, content)
        analysisBinding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return analysisBinding.root
    }

    private fun kindDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        kindBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_explain_kind, container, false)

        kindBinding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return kindBinding.root
    }

    private fun lvDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        lvBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_explain_lv, container, false)

        lvBinding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return lvBinding.root
    }

    private fun emotionDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        emotionBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_explain_emotion, container, false)

        emotionBinding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return emotionBinding.root
    }

    companion object {
        fun newInstance(
            dialogTag: String,
            content: String?,
        ): BottomSheetDialogFragment {
            return BottomSheetExplainDialog(dialogTag, content)
        }
    }
}