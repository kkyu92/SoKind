package com.sokind.ui.guide.tabs.manual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.data.remote.guide.Manual
import com.sokind.databinding.ItemManualBinding
import com.sokind.util.Constants
import com.sokind.util.OnManualItemClickListener
import com.sokind.util.ToggleAnimation
import java.util.concurrent.TimeUnit
import android.util.SparseBooleanArray

class ManualAdapter : RecyclerView.Adapter<ManualAdapter.ManualViewHolder>() {
    private lateinit var listener: OnManualItemClickListener
    var manualList: List<Manual> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManualViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemManualBinding.inflate(inflater, parent, false)
        return ManualViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ManualViewHolder, position: Int) {
        val manual = manualList[position]
        holder.bind(manual)
    }

    override fun getItemCount(): Int {
        return manualList.size
    }

    inner class ManualViewHolder(
        private val binding: ItemManualBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(manual: Manual) {
            binding.apply {
                tvManualPosition.text = "기본응대 - ${manual.position}"
                tvManualTitle.text = manual.title
                tvManualContent.text = manual.content

                ivExpand
                    .clicks()
                    .subscribe({
                        if (manual.content == "등록된 가이드가 없습니다.") {
                            val showNoManual = toggleLayout(!manual.isExpanded, ivExpand, llExpandNull)
                            manual.isExpanded = showNoManual
                        } else {
                            val showManual = toggleLayout(!manual.isExpanded, ivExpand, llExpand)
                            manual.isExpanded = showManual
                        }
                        listener.onManualItemClick(adapterPosition)
                    }, { it.printStackTrace() })
            }
        }

        private fun toggleLayout(
            isExpanded: Boolean,
            view: View,
            layoutExpand: LinearLayout
        ): Boolean {
            // 2
            ToggleAnimation.toggleArrow(view, isExpanded)
            if (isExpanded) {
                ToggleAnimation.expand(layoutExpand)
            } else {
                ToggleAnimation.collapse(layoutExpand)
            }
            return isExpanded
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setOnItemClickListener(listener: OnManualItemClickListener) {
        this.listener = listener
    }
}