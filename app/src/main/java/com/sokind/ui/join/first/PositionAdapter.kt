package com.sokind.ui.join.first

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.sokind.data.remote.member.join.Position
import com.sokind.databinding.ItemEnterpriseListBinding

class PositionAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val positionList: MutableList<Position>
) : ArrayAdapter<Position>(context, resId, positionList) {

    override fun getCount() = positionList.size


    override fun getItem(position: Int) = positionList[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val pos = positionList[position]
        try {
            binding.apply {
                tvEnterpriseList.text = pos.positionName
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val pos = positionList[position]
        try {
            binding.tvEnterpriseList.text = pos.positionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }
}