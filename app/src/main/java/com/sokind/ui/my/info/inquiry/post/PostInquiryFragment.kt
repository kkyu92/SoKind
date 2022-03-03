package com.sokind.ui.my.info.inquiry.post

import android.widget.RadioButton
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.checked
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FragmentPostInquiryBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.OnInquiryItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function5
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PostInquiryFragment :
    BaseFragment<FragmentPostInquiryBinding>(R.layout.fragment_post_inquiry) {
    private val viewModel by viewModels<PostInquiryViewModel>()
    private lateinit var listener: PostInquiryListener

    override fun init() {
        setBinding()
        setViewModel()
    }

    private fun setBinding() {
        binding.apply {
            compositeDisposable.add(
                Observable
                    .combineLatest(
                        typeApp.checkedChanges(),
                        typeEdu.checkedChanges(),
                        typeElse.checkedChanges(),
                        etTitle.textChanges(),
                        etContents.textChanges(),
                        Function5 { appChecked: Boolean, eduChecked: Boolean, elseChecked: Boolean,
                                    titleChanges: CharSequence, contentsChanges: CharSequence ->
                            return@Function5 (appChecked || eduChecked || elseChecked) &&
                                    titleChanges.toString()
                                        .isNotEmpty() && contentsChanges.toString().isNotEmpty()
                        }
                    )
                    .subscribe({ enable ->
                        inquirySend.isEnabled = enable
                    }, { it.printStackTrace() })
            )

            inquirySend
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val checked: RadioButton = typeGroup.findViewById(typeGroup.checkedRadioButtonId)
                    viewModel.postInquiry(
                        checked.text.toString(),
                        etTitle.text.toString(),
                        etContents.text.toString()
                    )
                }, { it.printStackTrace() })
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            postInquiry.observe(viewLifecycleOwner, {
                if (it) {
                    showToast("문의가 정상적으로 접수되었습니다.")
                    viewClear()
                    listener.onInquiryPost()
                }
            })
        }
    }

    private fun viewClear() {
        binding.apply {
            etTitle.text.clear()
            etContents.text.clear()
            typeApp.isChecked = true
            typeEdu.isChecked = false
            typeElse.isChecked = false
        }
    }

    interface PostInquiryListener {
        fun onInquiryPost()
    }

    fun setPostListener(listener: PostInquiryListener) {
        this.listener = listener
    }

    companion object {
        fun newInstance() = PostInquiryFragment()
    }
}