package com.sokind.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sokind.R
import com.sokind.data.remote.edu.CsBase
import com.sokind.databinding.ItemBaseCsBinding

class BaseCsAdapter(
    private val tabName: String
) : RecyclerView.Adapter<BaseCsAdapter.HomeCsViewHolder>() {
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
        return if (tabName == "Home") {
            val count = if (csBaseList.size > 5) {
                5
            } else {
                csBaseList.size
            }
            count
        } else {
            csBaseList.size
        }
    }

    inner class HomeCsViewHolder(
        private val binding: ItemBaseCsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(baseCs: CsBase) {
            binding.apply {
                tvCsBaseTitle.text = baseCs.title
                tvCsBaseSubTitle.text = baseCs.subTitle
                if (baseCs.isFinish) {
                    ivCsBaseBt.setImageResource(R.drawable.icon_play_btn_disable)
                    tvCsBaseState.text = "학습완료"
                    tvCsBaseState.setTextColor(binding.root.context.getColor(R.color.font_light_gray))
                    ivCsBaseDot.visibility = View.GONE
                } else {
                    ivCsBaseBt.setImageResource(R.drawable.icon_play_btn_enable)
                    tvCsBaseState.text = "학습하기"
                    tvCsBaseState.setTextColor(binding.root.context.getColor(R.color.main_color))
                    if (adapterPosition != 0 && !csBaseList[adapterPosition - 1].isFinish) {
                        ivCsBaseDot.visibility = View.GONE
                    }
                }
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