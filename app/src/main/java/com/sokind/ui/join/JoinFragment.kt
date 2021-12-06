package com.sokind.ui.join

import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FragmentJoinBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class JoinFragment : BaseFragment<FragmentJoinBinding>(R.layout.fragment_join) {
    private val viewModel by viewModels<JoinViewModel>()
    private lateinit var searchAdapter: EnterpriseSearchAdapter
    private val searchList: ArrayList<String> = arrayListOf()

    override fun init() {
        searchAdapter = EnterpriseSearchAdapter(requireContext(), R.layout.item_search_auto_complete, searchList)
        binding.apply {
            autoSearchView.setAdapter(searchAdapter)
            autoSearchView.setOnItemClickListener { adapterView, view, i, l ->
                Timber.e(searchAdapter.getObject(i))
            }
        }

        viewModel.autoText.observe(viewLifecycleOwner, {
            setAutoSearchView(it)
        })
    }

    private fun setAutoSearchView(list: List<String>) {
        searchAdapter.setData(list)
        searchAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "JoinFragment"
    }
}