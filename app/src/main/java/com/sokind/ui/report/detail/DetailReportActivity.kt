package com.sokind.ui.report.detail

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.ActivityDetailReportBinding
import com.sokind.ui.base.BaseActivity
import com.sokind.ui.report.detail.tabs.expression.ExpressionFragment
import com.sokind.ui.report.detail.tabs.posture.PostureFragment
import com.sokind.ui.report.detail.tabs.speech.SpeechFragment
import com.sokind.ui.report.detail.tabs.speech2.Speech2Fragment
import com.sokind.ui.report.detail.tabs.total.TotalFragment
import com.sokind.util.Constants
import com.sokind.util.adapter.TabAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DetailReportActivity :
    BaseActivity<ActivityDetailReportBinding>(R.layout.activity_detail_report) {
    private val viewModel by viewModels<DetailReportViewModel>()

    private lateinit var tabLayoutMediator: TabLayoutMediator

    private val totalFragment = TotalFragment()
    private val speechFragment = SpeechFragment()
    private val speech2Fragment = Speech2Fragment()
    private val expressionFragment = ExpressionFragment()
    private val postureFragment = PostureFragment()
    private val baseList = arrayListOf<Fragment>(
        totalFragment,
        speechFragment,
        expressionFragment,
        postureFragment
    )
    private val deepList = arrayListOf<Fragment>(
        totalFragment,
        speech2Fragment,
        expressionFragment,
        postureFragment
    )

    override fun init() {
        val type = intent.getIntExtra("type", 1)
        val key = intent.getIntExtra("key", 1)
        setViewModel(key, type)
        setTabLayout(type)
        setBinding()
    }

    private fun setViewModel(key: Int, type: Int) {
        viewModel.apply {
            getReportDetail(key, type)

            detailReport.observe(this@DetailReportActivity, {
                it.date
            })
        }
    }

    private fun setTabLayout(type: Int) {
        binding.apply {
            vpReportDetail.apply {
                offscreenPageLimit = 4
                adapter = if (type == 1) {
                    TabAdapter(baseList, supportFragmentManager, lifecycle)
                } else {
                    TabAdapter(deepList, supportFragmentManager, lifecycle)
                }
                isSaveEnabled = false
            }

            tabLayoutMediator = TabLayoutMediator(tlReportDetail, vpReportDetail) { tab, position ->
                tab.setCustomView(R.layout.item_tab)
                val title = tab.customView?.findViewById<TextView>(R.id.tv_tab_title)
                val icon = tab.customView?.findViewById<ImageView>(R.id.iv_tab_icon)
                when (position) {
                    0 -> {
                        title?.text = getString(R.string.total)
                        icon?.setBackgroundResource(R.drawable.selector_total)
                    }
                    1 -> {
                        title?.text = getString(R.string.speech)
                        icon?.setBackgroundResource(R.drawable.selector_speech)
                    }
                    2 -> {
                        title?.text = getString(R.string.expression)
                        icon?.setBackgroundResource(R.drawable.selector_expression)
                    }
                    3 -> {
                        title?.text = getString(R.string.posture)
                        icon?.setBackgroundResource(R.drawable.selector_posture)
                    }
                    else -> {
                        title?.text = getString(R.string.total)
                        icon?.setBackgroundResource(R.drawable.selector_total)
                    }
                }
            }
            tabLayoutMediator.attach()

            // viewpager different height
            vpReportDetail.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val view =
                        (vpReportDetail[0] as RecyclerView).layoutManager?.findViewByPosition(
                            position
                        )
                    view?.post {
                        val wMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                        val hMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        view.measure(wMeasureSpec, hMeasureSpec)
                        if (vpReportDetail.layoutParams.height != view.measuredHeight) {
                            vpReportDetail.layoutParams =
                                (vpReportDetail.layoutParams).also { lp ->
                                    lp.height = view.measuredHeight
                                }
                        }
                    }
                }
            })
        }
    }

    private fun setBinding() {
        binding.apply {
            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    setResult(RESULT_OK)
                    finish()
                }, { it.printStackTrace() })

            svSticky.run {
                header = tlReportDetail
                stickListener = { _ ->
                    Timber.e("stickListener")
                }
                freeListener = { _ ->
                    Timber.e("freeListener")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}