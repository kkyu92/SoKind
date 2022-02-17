package com.sokind.ui.report.detail.tabs.total

import android.content.Intent
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.report.ReportTotal
import com.sokind.databinding.FragmentTotalBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class TotalFragment (
    private val totalData: ReportTotal
) : BaseFragment<FragmentTotalBinding>(R.layout.fragment_total) {
    private val viewModel by viewModels<TotalViewModel>()

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        val stringUrl = totalData.recordFile
        val mScore = totalData.totalScore
        val avgScore = totalData.avgScore
        val per = totalData.per.toString() + "%"
        val gap = abs(mScore - avgScore).toString()

        binding.apply {
            chartProgress.tvKindPoint.text = totalData.totalScore.toString()
            when {
                totalData.avgScore < totalData.totalScore -> {
                    tvTotalComment.text = getString(R.string.total_comment_1, mScore.toString(), gap, per)
                }
                totalData.avgScore == totalData.totalScore -> {
                    tvTotalComment.text = getString(R.string.total_comment_2, mScore.toString(), per)
                }
                else -> {
                    tvTotalComment.text = getString(R.string.total_comment_3, mScore.toString(), gap, per)
                }
            }
            chartTriangle.setData(totalData.speakScore, totalData.emotionScore, totalData.postureScore)

            GlideApp.with(requireContext()).load(stringUrl).into(thumbnail)

            frameLayout
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(requireContext(), VideoViewActivity::class.java)
                    intent.putExtra("url", stringUrl)
                    startActivity(intent)
                }, { it.printStackTrace() })
        }
    }
}