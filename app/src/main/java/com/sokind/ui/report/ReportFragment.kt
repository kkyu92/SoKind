package com.sokind.ui.report

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.report.ReportItem
import com.sokind.databinding.FragmentReportBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.report.detail.DetailReportActivity
import com.sokind.util.BottomSheetExplainDialog
import com.sokind.util.Constants
import com.sokind.util.adapter.ReportAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report) {
    private val viewModel by viewModels<ReportViewModel>()

    private lateinit var reportBaseAdapter: ReportAdapter
    private lateinit var reportDeepAdapter: ReportAdapter
    val dummyData = mutableListOf<ReportItem>()

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            // reFresh
            Timber.e("reFresh")
        }
    }

    fun initializeList() { //임의로 데이터 넣어서 만들어봄
        with(dummyData) {
            add(ReportItem(1, "긍정 에너지를 전파하는 입점인사", null, 90))
            add(ReportItem(2, "제품에 불만이 있는 고객을 대할 때", null, 90))
            add(ReportItem(3, "긍정 에너지를 전파하는 입점인사", null, 90))
            add(ReportItem(4, "제품에 불만이 있는 고객을 대할 때", null, 90))
            add(ReportItem(5, "긍정 에너지를 전파하는 입점인사", null, 90))
            add(ReportItem(6, "제품에 불만이 있는 고객을 대할 때", null, 90))
            add(ReportItem(7, "긍정 에너지를 전파하는 입점인사", null, 90))
            add(ReportItem(8, "제품에 불만이 있는 고객을 대할 때", null, 90))
            add(ReportItem(9, "긍정 에너지를 전파하는 입점인사", null, 90))
            add(ReportItem(10, "제품에 불만이 있는 고객을 대할 때", null, 90))
            add(ReportItem(11, "긍정 에너지를 전파하는 입점인사", null, 90))
            add(ReportItem(12, "제품에 불만이 있는 고객을 대할 때", null, 90))
        }
    }

    override fun init() {
        initializeList()
        setRecyclerView()
        setBinding()
    }

    private fun setRecyclerView() {
        reportBaseAdapter = ReportAdapter(dummyData)
        binding.rvReportBase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReportBase.adapter = reportBaseAdapter
        reportBaseAdapter.setOnItemClickListener(object : ReportAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: ReportItem, pos: Int) {
                showToast("base : $pos")
                startDetailBase()
            }
        })

        reportDeepAdapter = ReportAdapter(dummyData)
        binding.rvReportDeep.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReportDeep.adapter = reportDeepAdapter
        reportDeepAdapter.setOnItemClickListener(object : ReportAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: ReportItem, pos: Int) {
                showToast("deep : $pos")
                startDetailDeep()
            }
        })
    }

    private fun setBinding() {
        binding.apply {
            tvKind1.text = getText(R.string.report_kind_point_1)
            tvKindAvg.text = "15점"
            tvKindPer.text = "10%"
            chartProgress.tvKindPoint.text = "90"
            tvQuality.text = getText(R.string.report_sample)

            tvKindTitle
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetExplainDialog.newInstance(
                        getString(R.string.kind_title),
                        getString(R.string.kind_content),
                        getString(R.string.kind_content2)
                    )
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })

            svSticky.run {
                header = reportBaseTitleContainer
                header2 = reportDeepTitleContainer
                stickListener = { _ ->
                    Timber.e("stickListener")
                }
                freeListener = { _ ->
                    Timber.e("freeListener")
                }
            }
        }
    }

    private fun startDetailBase() {
        val intent = Intent(requireContext(), DetailReportActivity::class.java)
        intent.putExtra("report", "base")
        startForResult.launch(intent)
    }
    private fun startDetailDeep() {
        val intent = Intent(requireContext(), DetailReportActivity::class.java)
        intent.putExtra("report", "deep")
        startForResult.launch(intent)
    }
}