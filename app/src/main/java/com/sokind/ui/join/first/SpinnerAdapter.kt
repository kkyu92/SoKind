package com.sokind.ui.join.first

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.sokind.databinding.ItemEnterpriseListBinding

class SpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val values: MutableList<String>
) : ArrayAdapter<String>(context, resId, values) {

    override fun getCount() = values.size


    override fun getItem(position: Int) = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val text = values[position]
        try {
            binding.apply {
                tvEnterpriseList.text = text
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val text = values[position]
        try {
            binding.tvEnterpriseList.text = text
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }
}