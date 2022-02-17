package com.sokind.util.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.databinding.DialogExplainBottomSheetBinding
import com.sokind.util.Constants
import java.util.concurrent.TimeUnit

class BottomSheetExplainDialog(
    private val title: String,
    private val content: String,
    private val content2: String?,
    private val img: Drawable?
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogExplainBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_explain_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvDialogTitle.text = title
            tvDialogContent.text = content
            tvDialogContent2.text = content2

            if (content2.isNullOrEmpty()) {
                tvDialogContent2.visibility = GONE
            }
            if (img != null) {
                ivDialog.visibility = View.VISIBLE
                GlideApp.with(requireContext()).load(img).into(ivDialog)
            }

            btCheck
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    dialog?.dismiss()
                }, { it.printStackTrace() })
        }

    }

    companion object {
        fun newInstance(
            title: String,
            content: String,
            content2: String?,
            img: Drawable?
        ): BottomSheetDialogFragment {
            return BottomSheetExplainDialog(title, content, content2, img)
        }
    }
}