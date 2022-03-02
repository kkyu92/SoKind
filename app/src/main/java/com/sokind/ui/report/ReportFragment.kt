package com.sokind.ui.report

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.edu.EduMapper.mappingReportToEdu
import com.sokind.data.remote.report.ReportItem
import com.sokind.data.remote.report.ReportResponse
import com.sokind.databinding.FragmentReportBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.report.detail.DetailReportActivity
import com.sokind.util.Constants
import com.sokind.util.adapter.ReportAdapter
import com.sokind.util.dialog.BottomSheetExplainDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report) {
    private val viewModel by viewModels<ReportViewModel>()

    private lateinit var reportResponse: ReportResponse
    private lateinit var reportBaseAdapter: ReportAdapter
    private lateinit var reportDeepAdapter: ReportAdapter

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // reFresh
                Timber.e("reFresh")
                viewModel.getReport()
            }
        }

    override fun init() {
        setBinding()
        setViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getReport()
        binding.scrollView.scrollTo(0, 0)
    }

    private fun setViewModel() {
        viewModel.apply {
            getMe.observe(viewLifecycleOwner, { user ->
                binding.apply {
                    GlideApp.with(requireContext())
                        .load(user.profileImg)
                        .error(R.drawable.icon_profile_default)
                        .into(ivCsProfile)
                    tvCsUserName.text =
                        getString(R.string.name_form, user.memberName, user.positionName)
                    tvCsUserEnterprise.text =
                        getString(R.string.enterprise_form, user.enterpriseName, user.storeName)
                }
            })

            report.observe(viewLifecycleOwner, {
                binding.apply {
                    if (it.reportList.isNullOrEmpty()) {
                        noReportContainer.visibility = View.VISIBLE
                        reportContainer.visibility = View.GONE
                    } else {
                        reportResponse = it
                        noReportContainer.visibility = View.GONE
                        reportContainer.visibility = View.VISIBLE
                        when {
                            it.allAvg < it.mAvg -> {
                                kindGap.text = "평균보다"
                                kindGapPoint.visibility = View.VISIBLE
                                kindGapText.text = "높아요"
                                underline.visibility = View.VISIBLE
                            }
                            it.allAvg > it.mAvg -> {
                                kindGap.text = "평균보다"
                                kindGapPoint.visibility = View.VISIBLE
                                kindGapText.text = "낮아요"
                                underline.visibility = View.VISIBLE
                            }
                            else -> {
                                kindGap.text = "평균과 일치해요"
                                kindGapPoint.visibility = View.GONE
                                kindGapText.text = ""
                                underline.visibility = View.GONE
                            }
                        }
                        pbCsLv.progress = it.lvRatio.toFloat()
                        tvCsLv.text = getString(R.string.lv, it.lv.toString())
                        tvCsDay.text = getString(R.string.cs_day, it.eduDate.toString())
                        tvAnalysisCount.text =
                            getString(R.string.report_analysis_count, it.analysisCnt.toString())
                        val gap = abs(it.allAvg - it.mAvg).toString()
                        kindGapPoint.text = getString(R.string.point, gap)
                        kindPer.text = getString(R.string.per, it.mPer.toInt().toString())
                        chartProgress.sbTotalPoint.progress = it.mAvg.toFloat()
                        chartProgress.tvKindPoint.text = it.mAvg.toString()

                        var comment = it.qualityComment.replace("'", "")
                        comment = comment.replace("%", "%%")
                        comment = getString(R.string.cdata, comment)
                        qualityComment.text = fromHtml(comment, null)

                        chartKind.avgKind.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            horizontalBias = it.allAvg.toFloat() / 100
                        }
                        chartKind.myKind.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            horizontalBias = it.mAvg.toFloat() / 100
                        }
                        chartTriangle.setData(it.speak, it.emotion, it.posture)
                        setRecyclerView(it.reportList)
                    }
                }
            })
        }
    }

    private fun setRecyclerView(reportList: List<ReportItem>) {
        val baseReportList: ArrayList<ReportItem> = arrayListOf()
        val deepReportList: ArrayList<ReportItem> = arrayListOf()
        for (report in reportList) {
            if (report.type == 1) {
                baseReportList.add(report)
            } else {
                deepReportList.add(report)
            }
        }

        reportBaseAdapter = ReportAdapter()
        reportBaseAdapter.reportList = baseReportList
        binding.rvReportBase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReportBase.adapter = reportBaseAdapter
        reportBaseAdapter.setOnItemClickListener(object : ReportAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: ReportItem, pos: Int) {
                reportItemClick(data)
            }
        })

        reportDeepAdapter = ReportAdapter()
        reportDeepAdapter.reportList = deepReportList
        binding.rvReportDeep.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReportDeep.adapter = reportDeepAdapter
        reportDeepAdapter.setOnItemClickListener(object : ReportAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: ReportItem, pos: Int) {
                reportItemClick(data)
            }
        })
    }

    private fun setBinding() {
        binding.apply {
            refreshLayout.setOnRefreshListener {
                viewModel.getReport()
                refreshLayout.isRefreshing = false
            }

            tvKind1.text = getText(R.string.report_kind_point_1)

            tvKindTitle
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetExplainDialog.newInstance(Constants.KIND_DIALOG, null)
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })

            tvAnalysisCount
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetExplainDialog.newInstance(
                        Constants.ANALYSIS_DIALOG,
                        reportResponse.analysisCnt.toString()
                    )
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })
        }
    }

    private fun reportItemClick(report: ReportItem) {
        when (report.status) {
            1 -> startDetail(report.key, report.type)
            4 -> showToast("분석중 새로고침")
            5 -> startEdu(mappingReportToEdu(report), startForResult)
            else -> Timber.e("report status : ${report.status}")
        }
    }

    private fun startDetail(key: Int, type: Int) {
        val intent = Intent(requireContext(), DetailReportActivity::class.java)
        intent.putExtra("key", key)
        intent.putExtra("type", type)
        startForResult.launch(intent)
    }
}