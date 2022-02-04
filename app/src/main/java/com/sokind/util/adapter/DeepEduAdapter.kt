package com.sokind.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.ItemDeepCsBinding
import com.sokind.util.Constants
import com.sokind.util.OnEduItemClickListener
import java.util.concurrent.TimeUnit


class DeepEduAdapter(
    private val tabName: String
) : RecyclerView.Adapter<DeepEduAdapter.HomeCsViewHolder>() {
    private lateinit var listener: OnEduItemClickListener
    var deepList: List<Edu> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDeepCsBinding.inflate(inflater, parent, false)
        return HomeCsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeCsViewHolder, position: Int) {
        val csDeep = deepList[position]
        holder.bind(csDeep)
    }

    override fun getItemCount(): Int {
        return if (tabName == "Home") {
            val count = if (deepList.size > 5) {
                5
            } else {
                deepList.size
            }
            count
        } else {
            deepList.size
        }
    }

    inner class HomeCsViewHolder(
        private val binding: ItemDeepCsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(deepEdu: Edu) {
             itemView
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    listener.onEduItemClick(deepEdu, adapterPosition)
                }, { it.printStackTrace() })

            binding.apply {
                tvDeepTitle.text = deepEdu.title
                tvDeepContent.text = deepEdu.contents
                // deepEdu.thumbnail
                GlideApp.with(ivDeepThumbnail).load("https://picsum.photos/320/160").into(ivDeepThumbnail)

                when (deepEdu.status) {
                    1 -> {
                        tvDeepStatus.text = root.context.getString(R.string.edu_fin)
                        tvDeepStatus.setTextColor(root.context.getColor(R.color.font_light_gray))
                    }
                    2 -> {
                        tvDeepStatus.text = root.context.getString(R.string.edu_do)
                        tvDeepStatus.setTextColor(root.context.getColor(R.color.main_color))
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