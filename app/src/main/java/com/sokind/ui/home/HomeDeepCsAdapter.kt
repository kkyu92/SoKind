package com.sokind.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.sokind.data.remote.home.CsBase
import com.sokind.data.remote.home.CsDeep
import com.sokind.databinding.ItemDeepCsBinding

class HomeDeepCsAdapter : RecyclerView.Adapter<HomeDeepCsAdapter.HomeCsViewHolder>() {
    var csDeepList: List<CsDeep> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDeepCsBinding.inflate(inflater, parent, false)
        return HomeCsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeCsViewHolder, position: Int) {
        val csDeep = csDeepList[position]
        holder.bind(csDeep)
    }

    override fun getItemCount(): Int {
        val count = if (csDeepList.size > 5) {
            5
        } else {
            csDeepList.size
        }
        return count
    }

    inner class HomeCsViewHolder(
        private val binding: ItemDeepCsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(csDeep: CsDeep) {
            binding.apply {
                tvCsDeepTitle.text = csDeep.title
                tvCsDeepContent.text = csDeep.content
                ivCsDeepBg.setBackgroundColor(csDeep.background ?: 0)
                ivCsDeepBg.setImageResource(csDeep.src ?: 0)
            }
        }
    }
}