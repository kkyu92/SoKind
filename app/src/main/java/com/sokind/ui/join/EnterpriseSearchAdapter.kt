package com.sokind.ui.join

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.sokind.R
import timber.log.Timber

class EnterpriseSearchAdapter(
    val mContext: Context,
    val resource: Int,
    val list: MutableList<String>
) : ArrayAdapter<String>(mContext, resource, list), Filterable {

    override fun getCount(): Int = list.size
    override fun getItem(position: Int): String = list[position]
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_search_enterprise, null, false)
        val textView: TextView = view.findViewById(R.id.tv_search_enterprise)
        Timber.e("getview : ${list[position]}")
        textView.text = list[position]
        return view
    }

    override fun getFilter() = mfilter

    private var mfilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            val query =
                if (constraint != null && constraint.isNotEmpty()) autocomplete(constraint.toString())
                else arrayListOf()

            results.values = query
            results.count = query.size

            return results
        }


        private fun autocomplete(input: String): MutableList<String> {
            val results = arrayListOf<String>()

            for (i in list) {
                results.add(i)
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.count > 0) notifyDataSetChanged()
            else notifyDataSetInvalidated()
        }
    }


    fun getObject(position: Int) = list[position]

    fun setData(changedList: List<String>) {
        list.clear()
        list.addAll(changedList)
    }

    fun sendWord(word: String) {

    }
}