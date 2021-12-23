package com.sokind.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.DialogBottomSheetBinding
import java.util.concurrent.TimeUnit

class BottomSheetDialog(
    private val question: String,
    val itemClick: (Boolean) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvDialogQuestion.text = getString(R.string.dialog_logout)

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

    }

    companion object {
        fun newInstance(
            question: String,
            itemClick: (Boolean) -> Unit
        ): BottomSheetDialogFragment {
            return BottomSheetDialog(question, itemClick)
        }
    }
}