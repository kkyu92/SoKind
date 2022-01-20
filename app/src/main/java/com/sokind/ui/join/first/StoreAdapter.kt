package com.sokind.ui.join.first

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.sokind.data.remote.member.join.Store
import com.sokind.databinding.ItemEnterpriseListBinding

class StoreAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val storeList: MutableList<Store>
) : ArrayAdapter<Store>(context, resId, storeList) {

    override fun getCount() = storeList.size


    override fun getItem(position: Int) = storeList[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val store = storeList[position]
        try {
            binding.apply {
                tvEnterpriseList.text = store.storeName
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEnterpriseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val store = storeList[position]
        try {
            binding.tvEnterpriseList.text = store.storeName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }
}