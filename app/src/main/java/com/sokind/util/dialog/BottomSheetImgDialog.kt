package com.sokind.util.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.DialogImgBottomSheetBinding
import com.sokind.util.Constants
import java.util.concurrent.TimeUnit

class BottomSheetImgDialog(
    private val title: String,
    private val img: Drawable,
    private val contents: String?,
    val itemClick: (Boolean) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogImgBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_img_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvDialogTitle.text = title
            ivImg.setImageDrawable(img)
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
    }

    companion object {
        fun newInstance(
            title: String,
            img: Drawable,
            contents: String,
            itemClick: (Boolean) -> Unit
        ): BottomSheetDialogFragment {
            return BottomSheetImgDialog(title, img, contents, itemClick)
        }
    }
}