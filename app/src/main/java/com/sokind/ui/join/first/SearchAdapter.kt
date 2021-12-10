package com.sokind.ui.join.first

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.sokind.databinding.ItemSearchEnterpriseBinding

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

    fun setClear() {
        searchWord = ""
        enterpriseList.clear()
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(
        private val binding: ItemSearchEnterpriseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(enterprise: String) {
            itemView.setOnClickListener {
                listener.onItemClick(it, enterprise, adapterPosition)
            }
            itemView.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        hideKeyboard(binding.root)
                        false
                    }
                    else -> false
                }
            }
//            Timber.e("searchWord: $searchWord") // 올리
//            Timber.e("itemWord: $enterprise") // 기업명
//            Timber.e("indexOf: ${enterprise.indexOf(searchWord)}")
            if (searchWord.isNotEmpty()) {
                if (enterprise.indexOf(searchWord) == -1) {
                    binding.tvSearchEnterprise.text = enterprise
                } else {
                    val start: Int = enterprise.indexOf(searchWord[0])
//                    val end: Int = enterprise.indexOf(searchWord[searchWord.length - 1]) + 1
                    var end: Int = start
                    val spannableString = SpannableString(enterprise)
//
//                    for (i in 1 until searchWord.length) {
//                        if (enterprise.indexOf(searchWord[i]) == enterprise.indexOf(searchWord[i-1]) + 1) {
//                            Timber.e("searchWord[i] = ${enterprise.indexOf(searchWord[i])}")
//                            Timber.e("searchWord[i-1] + 1 = ${enterprise.indexOf(searchWord[i-1]) + 1}")
//                            end++
//                            Timber.e("$end")
//                        } else {
//                            break
//                        }
//                    }
                    for (i in searchWord.indices) {
                        if (enterprise[i] == searchWord[i]) {
                            end++
                        } else {
                            break
                        }
                    }

                    spannableString.apply {
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#6967F9")),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    binding.tvSearchEnterprise.text = spannableString
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