package com.sokind.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.ItemBaseCsBinding
import com.sokind.util.Constants
import com.sokind.util.OnEduItemClickListener
import java.util.concurrent.TimeUnit

abstract class EduAdapter<B: ViewDataBinding>(
    @LayoutRes val layoutId: Int,
    private val tabName: String
) : RecyclerView.Adapter<EduAdapter.EduCsViewHolder>() {
    private lateinit var _binding: B
    protected val binding = _binding
    
    lateinit var listener: OnEduItemClickListener
    var eduList: List<Edu> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EduCsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        _binding = DataBindingUtil.inflate(inflater, layoutId, parent, false)
        return EduCsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EduCsViewHolder, position: Int) {
        val csBase = eduList[position]
        holder.bind(csBase)
    }

    override fun getItemCount(): Int {
        return if (tabName == "Home") {
            val count = if (eduList.size > 5) {
                5
            } else {
                eduList.size
            }
            count
        } else {
            eduList.size
        }
    }

    open class EduCsViewHolder(
        binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(edu: Edu) {

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