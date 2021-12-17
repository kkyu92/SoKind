package com.sokind.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sokind.data.remote.home.CsBase
import com.sokind.databinding.ItemBaseCsBinding

class HomeBaseCsAdapter : RecyclerView.Adapter<HomeBaseCsAdapter.HomeCsViewHolder>() {
    var csBaseList: List<CsBase> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBaseCsBinding.inflate(inflater, parent, false)
        return HomeCsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeCsViewHolder, position: Int) {
        val csBase = csBaseList[position]
        holder.bind(csBase)
    }

    override fun getItemCount(): Int {
        val count = if (csBaseList.size > 5) {
            5
        } else {
            csBaseList.size
        }
        return count
    }

    inner class HomeCsViewHolder(
        private val binding: ItemBaseCsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(baseCs: CsBase) {
            binding.apply {
                tvCsBaseTitle.text = baseCs.title
                tvCsBaseSubTitle.text = baseCs.subTitle
                tvCsBaseContent.text = baseCs.content
                tvCsBaseTime.text = baseCs.time

                if (baseCs.content.isNullOrEmpty()) {
                    tvCsBaseContent.visibility = View.GONE
                }
            }
        }
    }
}