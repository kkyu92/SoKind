package com.sokind.ui.my.info.inquiry

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.common.Inquiry
import com.sokind.data.remote.guide.Manual
import com.sokind.databinding.ItemInquiryBinding
import com.sokind.databinding.ItemManualBinding
import com.sokind.util.OnInquiryItemClickListener
import com.sokind.util.OnManualItemClickListener
import com.sokind.util.ToggleAnimation

class InquiryAdapter : RecyclerView.Adapter<InquiryAdapter.InquiryViewHolder>() {
    private lateinit var listener: OnInquiryItemClickListener
    var inquiryList: List<Inquiry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInquiryBinding.inflate(inflater, parent, false)
        return InquiryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InquiryViewHolder, position: Int) {
        val inquiry = inquiryList[position]
        holder.bind(inquiry)
    }

    override fun getItemCount(): Int {
        return inquiryList.size
    }

    inner class InquiryViewHolder(
        private val binding: ItemInquiryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(inquiry: Inquiry) {
            binding.apply {
                title.text = inquiry.title
                if (inquiry.replyFlag == 0) {
                    date.text =
                        "${inquiry.createdAt}  |  ${binding.root.context.getString(R.string.inquiry_ing)}"
                } else {
                    date.text =
                        inquiry.createdAt + "  |  " + binding.root.context.getText(R.string.inquiry_end)
                }
                content.text = inquiry.contents

                ivExpand
                    .clicks()
                    .subscribe({
                        val showManual = toggleLayout(!inquiry.isExpanded, ivExpand, llExpand)
                        inquiry.isExpanded = showManual
                        listener.onInquiryItemClick(adapterPosition)
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

    fun setOnItemClickListener(listener: OnInquiryItemClickListener) {
        this.listener = listener
    }
}