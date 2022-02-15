package com.sokind.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.BaseEdu
import com.sokind.databinding.ItemBaseCsBinding
import com.sokind.util.Constants
import com.sokind.util.OnEduItemClickListener
import java.util.concurrent.TimeUnit

class BaseEduAdapter(
    private val tabName: String
) : RecyclerView.Adapter<BaseEduAdapter.HomeCsViewHolder>() {
    private lateinit var listener: OnEduItemClickListener
    var baseList: List<BaseEdu> = listOf()

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

        fun bind(baseEdu: BaseEdu) {
            itemView
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    listener.onBaseItemClick(baseEdu, adapterPosition)
                }, { it.printStackTrace() })

            binding.apply {
                tvBaseTitle.text = "기본응대 - ${adapterPosition + 1}"
                tvBaseContents.text = baseEdu.title

                when (baseEdu.status) {
                    1 -> {
                        ivBaseBt.setImageResource(R.drawable.icon_play_btn_disable)
                        tvBaseState.text = root.context.getString(R.string.edu_fin)
                        tvBaseState.setTextColor(root.context.getColor(R.color.font_light_gray))
                    }
                    2 -> {
                        ivBaseBt.setImageResource(R.drawable.icon_play_btn_enable)
                        tvBaseState.text = root.context.getString(R.string.edu_do)
                        tvBaseState.setTextColor(root.context.getColor(R.color.main_color))
                    }
                    3 -> { // 진행중

                    }
                    4 -> { // 분석중
                        ivBaseBt.setImageResource(R.drawable.icon_analysis)
                        tvBaseState.text = root.context.getString(R.string.edu_analysis)
                        tvBaseState.setTextColor(root.context.getColor(R.color.font_light_gray))
                    }
                    5 -> { // 분석오류
                        ivBaseBt.setImageResource(R.drawable.icon_analysis)
                        tvBaseState.text = root.context.getString(R.string.edu_analysis_fail)
                        tvBaseState.setTextColor(root.context.getColor(R.color.font_light_gray))
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

    fun setOnItemClickListener(listener: OnEduItemClickListener) {
        this.listener = listener
    }
}