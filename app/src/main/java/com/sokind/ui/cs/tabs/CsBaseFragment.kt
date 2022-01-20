package com.sokind.ui.cs.tabs

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.CsBase
import com.sokind.databinding.FragmentCsBaseBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.ShowCsFragmentListener
import com.sokind.util.adapter.BaseCsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CsBaseFragment(
    private val tabName: String
) : BaseFragment<FragmentCsBaseBinding>(R.layout.fragment_cs_base) {

    private lateinit var showCsListener: ShowCsFragmentListener
    private lateinit var baseCsAdapter: BaseCsAdapter
    val dummyData = mutableListOf<CsBase>()

    override fun init() {
        initializeList()
        setRecyclerView()
        setBinding()
    }

    fun initializeList(){ //임의로 데이터 넣어서 만들어봄
        with(dummyData){
            add(CsBase("기본응대 - 1", "긍정 에너지를 전파하는 입점인사", true))
            add(CsBase("상황응대 - 1", "제품에 불만이 있는 고객을 대할 때", true))
            add(CsBase("기본응대 - 2", "긍정 에너지를 전파하는 입점인사", true))
            add(CsBase("상황응대 - 2", "제품에 불만이 있는 고객을 대할 때", false))
            add(CsBase("기본응대 - 1", "긍정 에너지를 전파하는 입점인사", false))
            add(CsBase("상황응대 - 1", "제품에 불만이 있는 고객을 대할 때", false))
            add(CsBase("기본응대 - 2", "긍정 에너지를 전파하는 입점인사", false))
            add(CsBase("상황응대 - 2", "제품에 불만이 있는 고객을 대할 때", false))
            add(CsBase("기본응대 - 1", "긍정 에너지를 전파하는 입점인사", false))
            add(CsBase("상황응대 - 1", "제품에 불만이 있는 고객을 대할 때", false))
            add(CsBase("기본응대 - 2", "긍정 에너지를 전파하는 입점인사", false))
            add(CsBase("상황응대 - 2", "제품에 불만이 있는 고객을 대할 때", false))
        }
    }

    private fun setRecyclerView() {
        baseCsAdapter = BaseCsAdapter(tabName)
        baseCsAdapter.csBaseList = dummyData
        binding.rvHomeBaseCs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeBaseCs.adapter = baseCsAdapter
    }

    private fun setBinding() {
        binding.apply {
            tvBaseMore
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    showCsListener.showCsFragment()
                },{ it.printStackTrace() })

            if (tabName == "Cs") {
                tvBaseMore.visibility = View.GONE
            }
        }
    }

    companion object {

    }
}