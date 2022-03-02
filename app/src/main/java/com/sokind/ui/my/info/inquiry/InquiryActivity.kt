package com.sokind.ui.my.info.inquiry

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sokind.R
import com.sokind.databinding.ActivityInquiryBinding
import com.sokind.ui.base.BaseActivity
import com.sokind.ui.my.info.inquiry.my.MyInquiryFragment
import com.sokind.ui.my.info.inquiry.post.PostInquiryFragment
import com.sokind.util.adapter.TabAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InquiryActivity : BaseActivity<ActivityInquiryBinding>(R.layout.activity_inquiry) {
    private lateinit var tabLayoutMediator: TabLayoutMediator

    private val postInquiry = PostInquiryFragment()
    private val myInquiry = MyInquiryFragment()
    private val fragmentList = arrayListOf<Fragment>(
        postInquiry,
        myInquiry
    )

    override fun init() {
        setTabLayout()
    }

    private fun setTabLayout() {
        binding.apply {
            vpInquiry.apply {
                offscreenPageLimit = 2
                adapter = TabAdapter(fragmentList, supportFragmentManager, lifecycle)
                isSaveEnabled = false
            }

            tabLayoutMediator = TabLayoutMediator(tlInquiry, vpInquiry) { tab, position ->
                when (position) {
                    0 -> tab.text = "문의하기"
                    1 -> tab.text = "나의 문의내역"
                    else -> tab.text = "문의하기"
                }
            }
            tabLayoutMediator.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }

}