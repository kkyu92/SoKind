package com.sokind.ui.cs.tabs

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.CsBase
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.FragmentCsBaseBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.ShowCsFragmentListener
import com.sokind.util.adapter.BaseEduAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CsBaseFragment(
    private val baseList: List<Edu>
) : BaseFragment<FragmentCsBaseBinding>(R.layout.fragment_cs_base) {

    private lateinit var showCsListener: ShowCsFragmentListener
    private lateinit var baseEduAdapter: BaseEduAdapter

    override fun init() {
        setRecyclerView()
        setBinding()
    }

    private fun setRecyclerView() {
        baseEduAdapter = BaseEduAdapter("CS")
        baseEduAdapter.baseList = baseList
        binding.rvHomeBaseCs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeBaseCs.adapter = baseEduAdapter
    }

    private fun setBinding() {
        binding.apply {

        }
    }

    companion object {

    }
}