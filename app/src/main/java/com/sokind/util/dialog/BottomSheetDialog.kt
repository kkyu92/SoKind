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

class BottomSheetDialog(
    private val dialogTag: String,
    private val title: String,
    private val contents: String?,
    val itemClick: (Boolean) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var listener: OnProfileClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        when (dialogTag) {
            Constants.SIMPLE_DIALOG -> return simpleDialog(inflater, container)
            Constants.CHANGE_DIALOG -> return changeDialog(inflater, container)

            Constants.PROFILE_DIALOG -> return profileDialog(inflater, container)

            Constants.ANALYSIS_ERROR_DIALOG -> return analysisErrorDialog(inflater, container)
            Constants.SECESSION_DIALOG -> return secessionDialog(inflater, container)
        }

        return simpleDialog(inflater, container)
    }

    private fun secessionDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogSecessionBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_secession, container, false)

        binding.btOk
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                itemClick(false)
                dialog?.dismiss()
            }, { it.printStackTrace() })

        binding.dialogJoin
            .clicks()
            .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
            .subscribe({
                itemClick(true)
                dialog?.dismiss()
            }, { it.printStackTrace() })

        return binding.root
    }

    private fun simpleDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogBottomSheetBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet, container, false)

        binding.apply {
            tvDialogTitle.text = title
            tvDialogContents.text = contents
            btNo
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    itemClick(false)
                    dialog?.dismiss()
                }, { it.printStackTrace() })

            btYes
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    itemClick(true)
                    dialog?.dismiss()
                }, { it.printStackTrace() })
        }

        return binding.root
    }

    private fun changeDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogChangeBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_change, container, false)

        binding.apply {
            tvDialogTitle.text = title
            tvDialogContents.text = contents

            btYes
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    itemClick(true)
                    dialog?.dismiss()
                }, { it.printStackTrace() })
        }

        return binding.root
    }

    private fun profileDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_profile, container, false)

        binding.apply {
            tvDialogTitle.text = title

            camera
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    listener.onCameraClick()
                }, { it.printStackTrace() })

            gallery
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    listener.onGalleryClick()
                }, { it.printStackTrace() })

            btDefault
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    itemClick(false)
                    dialog?.dismiss()
                }, { it.printStackTrace() })

            btSave
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    itemClick(true)
                    dialog?.dismiss()
                }, { it.printStackTrace() })
        }

        return binding.root
    }

    private fun analysisErrorDialog(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogErrorAnalysisBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_error_analysis, container, false)

        binding.apply {
            tvDialogTitle.text = title
            ivImg.setImageResource(R.drawable.img_error)
            tvDialogContents.text = contents
            btOk
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    itemClick(true)
                    dialog?.dismiss()
                }, { it.printStackTrace() })
            btOk
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    itemClick(false)
                    dialog?.dismiss()
                }, { it.printStackTrace() })
        }

        return binding.root
    }

    interface OnProfileClickListener {
        fun onCameraClick()
        fun onGalleryClick()
    }

    fun setOnProfileClickListener(listener: OnProfileClickListener) {
        this.listener = listener
    }

    companion object {
        fun newInstance(
            tag: String,
            title: String,
            contents: String,
            itemClick: (Boolean) -> Unit
        ): BottomSheetDialogFragment {
            return BottomSheetDialog(tag, title, contents, itemClick)
        }
    }
}