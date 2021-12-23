package com.sokind.ui.guide.tabs.manual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.data.remote.guide.Manual
import com.sokind.databinding.ItemManualBinding
import com.sokind.util.ToggleAnimation
import timber.log.Timber

class ManualAdapter : RecyclerView.Adapter<ManualAdapter.ManualViewHolder>() {
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
                tvManualTitle.text = manual.title
                tvManualSubTitle.text = manual.subTitle
                tvManualContent.text = manual.content

//                llExpand.visibility = if (manual.isExpanded) View.VISIBLE else View.GONE
//                Timber.e("${manual.isExpanded}")
                ivExpand
                    .clicks()
                    .subscribe({
                        val show = toggleLayout(!manual.isExpanded, ivExpand, llExpand)
                        for (i in manualList.indices) {
                            if (i != adapterPosition) {
                                manualList[i].isExpanded = !show
                            }
                        }
//                        notifyDataSetChanged()
                        manual.isExpanded = show
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
}