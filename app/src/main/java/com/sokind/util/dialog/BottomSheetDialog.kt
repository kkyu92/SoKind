package com.sokind.util.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.DialogBottomSheetBinding
import com.sokind.util.Constants
import java.util.concurrent.TimeUnit

class BottomSheetDialog(
    private val title: String,
    private val contents: String?,
    val itemClick: (Boolean) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

    companion object {
        fun newInstance(
            title: String,
            contents: String,
            itemClick: (Boolean) -> Unit
        ): BottomSheetDialogFragment {
            return BottomSheetDialog(title, contents, itemClick)
        }
    }
}