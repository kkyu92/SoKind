package com.sokind.ui.my.notice

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.common.Notice
import com.sokind.databinding.ActivityNoticeBinding
import com.sokind.ui.base.BaseActivity
import com.sokind.util.Constants
import com.sokind.util.OnNoticeItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NoticeActivity : BaseActivity<ActivityNoticeBinding>(R.layout.activity_notice) {
    private val viewModel by viewModels<NoticeViewModel>()

    private lateinit var adapter: NoticeAdapter

    override fun init() {
        setBinding()
        setViewModel()
    }

    private fun setBinding() {
        binding.apply {
            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    onBackPressed()
                }, { it.printStackTrace() })
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            getNotice.observe(this@NoticeActivity, {
                setData(it.noticeList)
            })
        }
    }

    private fun setData(noticeList: List<Notice>) {
        binding.apply {
            adapter = NoticeAdapter()
            adapter.noticeList = noticeList
            rvNotice.setHasFixedSize(true)
            rvNotice.layoutManager = LinearLayoutManager(this@NoticeActivity)
            rvNotice.adapter = adapter

            adapter.setOnItemClickListener(object : OnNoticeItemClickListener {
                override fun onNoticeItemClick(pos: Int) {
                    compositeDisposable.add(
                        Completable.complete()
                            .delay(200, TimeUnit.MILLISECONDS)
                            .subscribe({
                                rvNotice.smoothScrollToPosition(pos)
                            }, { it.printStackTrace() })
                    )
                }
            })
        }
    }
}