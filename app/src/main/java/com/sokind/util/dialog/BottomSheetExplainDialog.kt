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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        when (dialogTag) {
            Constants.EMOTION_DIALOG -> return emotionDialog(inflater, container)
            Constants.LV_DIALOG -> return lvDialog(inflater, container)
            Constants.KIND_DIALOG -> return kindDialog(inflater, container)
            Constants.ANALYSIS_DIALOG -> return analysisDialog(inflater, container)
            Constants.COMPLAIN_DIALOG -> return complainDialog(inflater, container)
            Constants.SPEED_DIALOG -> return speechDialog(inflater, container)
            Constants.SECESSION_DIALOG -> return secessionDialog(inflater, container)
        }

        return emotionDialog(inflater, container)
    }

    private fun secessionDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogExplainSecessionBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_explain_secession, container, false)

        if (content == Constants.SECESSION_LOGIN_REQUEST.toString()) {
            binding.tvDialogContents.text = getString(R.string.dialog_secession_request_title)
        } else if (content == Constants.SECESSION_LOGIN.toString()) {
            binding.tvDialogContents.text = getString(R.string.dialog_secession_title)
        }
        binding.btYes
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
    }

    private fun speechDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogExplainSpeechBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_explain_speech, container, false)

        binding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
    }

    private fun complainDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogExplainComplainBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_explain_complain, container, false)

        binding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
    }

    private fun analysisDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogExplainAnalysisBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_explain_analysis, container, false)

        binding.tvContent.text = getString(R.string.analysis_content, content)
        binding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
    }

    private fun kindDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogExplainKindBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_explain_kind, container, false)

        binding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
    }

    private fun lvDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogExplainLvBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_explain_lv, container, false)

        binding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
    }

    private fun emotionDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogExplainEmotionBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_explain_emotion, container, false)

        binding.btCheck
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
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