package com.sokind.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.data.remote.report.ReportItem
import com.sokind.databinding.ItemReportBinding
import com.sokind.util.Constants
import java.util.concurrent.TimeUnit

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
    private lateinit var listener: OnItemClickListener
    var reportList: List<ReportItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReportBinding.inflate(inflater, parent, false)
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val reportItem = reportList[position]
        holder.bind(reportItem)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    inner class ReportViewHolder(
        private val binding: ItemReportBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(report: ReportItem) {
            binding.apply {
                tvNum.text = (adapterPosition + 1).toString()
                tvTitle.text = report.title
                if (report.subTitle == "none") {
                    tvSubTitle.visibility = View.GONE
                }
                tvSubTitle.text = report.subTitle
                setListData(report)
            }

            itemView
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    listener.onItemClick(itemView, report, adapterPosition)
                }, { it.printStackTrace() })
        }

        private fun setListData(report : ReportItem) {
            binding.apply {
                when (report.status) {
                    1 -> {
                        dataContainer.visibility = View.VISIBLE
                        noDataContainer.visibility = View.GONE
                        tvPoint.text = report.score.toString()
                        tvAnalysis.text = "학습완료"
                    }
                    2 -> {
                        dataContainer.visibility = View.VISIBLE
                        noDataContainer.visibility = View.GONE
                        tvPoint.text = report.score.toString()
                        tvAnalysis.text = "학습하기"
                    }
                    3 -> { // 진행중

                    }
                    4 -> {
                        dataContainer.visibility = View.GONE
                        noDataContainer.visibility = View.VISIBLE
                        tvAnalysis.text = "분석중"
                    }
                    5 -> {
                        dataContainer.visibility = View.GONE
                        noDataContainer.visibility = View.VISIBLE
                        tvAnalysis.text = "분석오류"
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ReportItem, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}