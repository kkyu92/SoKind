package com.sokind.ui.my.info.inquiry.my

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.common.Inquiry
import com.sokind.databinding.FragmentMyInquiryBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.my.info.inquiry.InquiryAdapter
import com.sokind.util.OnInquiryItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MyInquiryFragment : BaseFragment<FragmentMyInquiryBinding>(R.layout.fragment_my_inquiry) {
    private val viewModel by viewModels<MyInquiryViewModel>()

    private lateinit var adapter: InquiryAdapter

    override fun init() {

        setViewModel()
    }

    private fun setViewModel() {
        viewModel.apply {
            getInquiry.observe(viewLifecycleOwner, {
                setData(it.inquiryList)
            })
        }
    }

    private fun setData(inquiryList: List<Inquiry>) {
        binding.apply {
            adapter = InquiryAdapter()
            adapter.inquiryList = inquiryList
            rvInquiry.setHasFixedSize(true)
            rvInquiry.layoutManager = LinearLayoutManager(requireContext())
            rvInquiry.adapter = adapter

            adapter.setOnItemClickListener(object : OnInquiryItemClickListener {
                override fun onInquiryItemClick(pos: Int) {
                    compositeDisposable.add(
                        Completable.complete()
                            .delay(200, TimeUnit.MILLISECONDS)
                            .subscribe({
                                rvInquiry.smoothScrollToPosition(pos)
                            }, { it.printStackTrace() })
                    )
                }
            })

            if (inquiryList.isEmpty()) {
                rvInquiry.visibility = View.GONE
                inquiryEmpty.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun newInstance() = MyInquiryFragment()
    }
}