package com.sokind.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sokind.data.remote.home.CsDeep
import com.sokind.databinding.ItemDeepCsBinding

class DeepCsAdapter(
    private val tabName: String
) : RecyclerView.Adapter<DeepCsAdapter.HomeCsViewHolder>() {
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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}