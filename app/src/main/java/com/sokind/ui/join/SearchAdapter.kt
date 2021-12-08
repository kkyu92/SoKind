package com.sokind.ui.join

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.sokind.databinding.ItemSearchEnterpriseBinding
import timber.log.Timber

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var enterpriseList: MutableList<String> = mutableListOf()
    private lateinit var searchWord: String
    private lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchEnterpriseBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val enterprise = enterpriseList[position]
        holder.bind(enterprise)
    }

    override fun getItemCount(): Int = enterpriseList.size

    fun setData(changedList: List<String>, word: String) {
        searchWord = word
        enterpriseList.clear()
        enterpriseList.addAll(changedList)
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchEnterpriseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(enterprise: String) {
            itemView.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        hideKeyboard(binding.root)
                    }
                }
                true
            }
            itemView.setOnClickListener {
                listener.onItemClick(it, enterprise, adapterPosition)
            }
            Timber.e("searchWord: $searchWord")
            Timber.e("itemWord: $enterprise")
            Timber.e("indexOf: ${enterprise.indexOf(searchWord)}")
            for (i in searchWord.indices) {
                //searchWord 하나씩 앞에거 짤라서 쓰기
                if (enterprise.indexOf(searchWord) != -1) {
                    val spannableString = SpannableString(enterprise)
                    val start: Int = enterprise.indexOf(searchWord)
                    val end: Int = enterprise.indexOf(searchWord)

                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#6967F9")),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    binding.tvSearchEnterprise.text = spannableString
                } else {
                    binding.tvSearchEnterprise.text = enterprise
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, enterprise: String, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun hideKeyboard(root: View) {
        root.clearFocus()
        val imm =
            root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(root.windowToken, 0)
    }
}