package com.sokind.util.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.sokind.data.remote.member.join.Store
import com.sokind.databinding.ItemEnterpriseListBinding

class SpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val reasonList: MutableList<String>
) : ArrayAdapter<String>(context, resId, reasonList) {

    override fun getCount() = reasonList.size


    override fun getItem(position: Int) = reasonList[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val reason = reasonList[position]
        try {
            binding.apply {
                tvEnterpriseList.text = reason
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val reason = reasonList[position]
        try {
            binding.tvEnterpriseList.text = reason
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }
}