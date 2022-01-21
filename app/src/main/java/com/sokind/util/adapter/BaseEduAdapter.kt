package com.sokind.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.ItemBaseCsBinding

class BaseEduAdapter(
    private val tabName: String
) : RecyclerView.Adapter<BaseEduAdapter.HomeCsViewHolder>() {
    var baseList: List<Edu> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBaseCsBinding.inflate(inflater, parent, false)
        return HomeCsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeCsViewHolder, position: Int) {
        val csBase = baseList[position]
        holder.bind(csBase)
    }

    override fun getItemCount(): Int {
        return if (tabName == "Home") {
            val count = if (baseList.size > 5) {
                5
            } else {
                baseList.size
            }
            count
        } else {
            baseList.size
        }
    }

    inner class HomeCsViewHolder(
        private val binding: ItemBaseCsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(baseEdu: Edu) {
            binding.apply {
                tvBaseTitle.text = baseEdu.title
                tvBaseContents.text = baseEdu.contents

                when(baseEdu.status) {
                    1 -> {
                        ivBaseBt.setImageResource(R.drawable.icon_play_btn_disable)
                        tvBaseState.text = root.context.getString(R.string.edu_fin)
                        tvBaseState.setTextColor(root.context.getColor(R.color.font_light_gray))
                        ivCsBaseDot.visibility = View.GONE
                    }
                    2 -> {
                        ivBaseBt.setImageResource(R.drawable.icon_play_btn_enable)
                        tvBaseState.text = root.context.getString(R.string.edu_do)
                        tvBaseState.setTextColor(root.context.getColor(R.color.main_color))
                        if (adapterPosition != 0 && baseList[adapterPosition - 1].status != 1) {
                            ivCsBaseDot.visibility = View.GONE
                        }
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