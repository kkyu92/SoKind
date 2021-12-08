package com.sokind.ui.join

import android.text.Html
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FragmentJoinBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class JoinFragment : BaseFragment<FragmentJoinBinding>(R.layout.fragment_join) {
    private val viewModel by viewModels<JoinViewModel>()
//    private lateinit var searchAdapter: EnterpriseSearchAdapter
    private lateinit var searchAdapter: SearchAdapter
    private val searchList: ArrayList<String> = arrayListOf()

    override fun init() {
//        searchAdapter = EnterpriseSearchAdapter(requireContext(), R.layout.item_search_enterprise, searchList)
        searchAdapter = SearchAdapter()

        binding.apply {
//            autoSearchView.setAdapter(searchAdapter)
//            autoSearchView.setOnItemClickListener { adapterView, view, i, l ->
//                Timber.e(searchAdapter.getObject(i))
//            }
            rvEnterprise.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvEnterprise.adapter = searchAdapter
            searchAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
                override fun onItemClick(v: View, enterprise: String, pos: Int) {
                    autoSearchView.setText(enterprise)
                    rvEnterprise.visibility = View.GONE
                    etEnterpriseCodeInput.visibility = View.VISIBLE
                }
            })

            autoSearchView
                .textChanges()
                .subscribe({
                    Timber.e("length: ${it.length}")
                    if (it.isNotEmpty()) {
                        viewModel.searchEnterprise(it)
                    }
                }, { it.printStackTrace() })
        }

        viewModel.autoText.observe(viewLifecycleOwner, {
            val searchWord = binding.autoSearchView.text.toString()

            if (it.isEmpty()) {
                binding.rvEnterprise.visibility = View.GONE
                binding.tvNoEnterprise.text = Html.fromHtml(String.format(getString(R.string.no_enterprise_content_1), searchWord))
                binding.llNoEnterpriseResult.visibility = View.VISIBLE
            } else {
                setAutoSearchView(it, searchWord)
            }
        })
    }

    private fun setAutoSearchView(list: List<String>, word: String) {
        searchAdapter.setData(list, word)
//        searchAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "JoinFragment"
    }
}