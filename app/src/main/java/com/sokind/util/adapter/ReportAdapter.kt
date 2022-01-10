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

class ReportAdapter(
    private var reportList: List<ReportItem> = listOf()
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
    private var listener: OnItemClickListener? = null

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
                tvSubTitle.text = report.subTitle
                tvPoint.text = report.point.toString()
            }
            itemView
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    listener?.onItemClick(itemView, report, adapterPosition)
                }, { it.printStackTrace() })
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, data: ReportItem, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}