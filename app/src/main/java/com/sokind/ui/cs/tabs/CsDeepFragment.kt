package com.sokind.ui.cs.tabs

import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.edu.CsDeep
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.FragmentCsDeepBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.adapter.DeepEduAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CsDeepFragment(
    private val deepList: List<Edu>
) : BaseFragment<FragmentCsDeepBinding>(R.layout.fragment_cs_deep) {

    private lateinit var deepEduAdapter: DeepEduAdapter

    override fun init() {
        setRecyclerView()
    }

    private fun setRecyclerView() {
        deepEduAdapter = DeepEduAdapter("CS")
        deepEduAdapter.deepList = deepList
        binding.rvHomeDeepCs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeDeepCs.adapter = deepEduAdapter
    }

    companion object {

    }
}