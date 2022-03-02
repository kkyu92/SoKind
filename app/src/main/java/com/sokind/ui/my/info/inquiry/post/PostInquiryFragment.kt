package com.sokind.ui.my.info.inquiry.post

import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FragmentPostInquiryBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostInquiryFragment :
    BaseFragment<FragmentPostInquiryBinding>(R.layout.fragment_post_inquiry) {
    private val viewModel by viewModels<PostInquiryViewModel>()

    override fun init() {

    }

    companion object {
        fun newInstance() = PostInquiryFragment()
    }
}