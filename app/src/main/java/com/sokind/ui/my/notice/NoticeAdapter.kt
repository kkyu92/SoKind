package com.sokind.ui.my.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.data.remote.common.Notice
import com.sokind.databinding.ItemNoticeBinding
import com.sokind.util.OnNoticeItemClickListener
import com.sokind.util.ToggleAnimation

class NoticeAdapter : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {
    private lateinit var listener: OnNoticeItemClickListener
    var noticeList: List<Notice> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoticeBinding.inflate(inflater, parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val notice = noticeList[position]
        holder.bind(notice)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    inner class NoticeViewHolder(
        private val binding: ItemNoticeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(notice: Notice) {
            binding.apply {
                title.text = notice.title
                date.text = notice.createdAt
                content.text = notice.contents

                ivExpand
                    .clicks()
                    .subscribe({
                        val showManual = toggleLayout(!notice.isExpanded, ivExpand, llExpand)
                        notice.isExpanded = showManual
                        listener.onNoticeItemClick(adapterPosition)
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

    fun setOnItemClickListener(listener: OnNoticeItemClickListener) {
        this.listener = listener
    }
}