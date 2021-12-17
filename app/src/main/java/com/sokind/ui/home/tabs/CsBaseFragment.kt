package com.sokind.ui.home.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.home.CsBase
import com.sokind.databinding.FragmentCsBaseBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.home.HomeBaseCsAdapter
import com.sokind.util.Constants
import com.sokind.util.ShowFragmentListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CsBaseFragment : BaseFragment<FragmentCsBaseBinding>(R.layout.fragment_cs_base) {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listener: ShowFragmentListener
    private lateinit var homeBaseCsAdapter: HomeBaseCsAdapter
    val dummyData = mutableListOf<CsBase>()

    override fun init() {
        initializeList()
        setRecyclerView()
        setBinding()

        arguments?.let {
            param1 = it.getString("ARG_PARAM1")
            param2 = it.getString("ARG_PARAM2")
        }
    }

    fun initializeList(){ //임의로 데이터 넣어서 만들어봄
        with(dummyData){
            add(CsBase("기본응대 - 1","긍정 에너지를 전파하는 입점인사",null,"10분 전"))
            add(CsBase("상황응대 - 1","제품에 불만이 있는 고객을 대할 때",null,"2일 전"))
            add(CsBase("기본응대 - 2","긍정 에너지를 전파하는 입점인사",null,"10분 전"))
            add(CsBase("상황응대 - 2","제품에 불만이 있는 고객을 대할 때",null,"2일 전"))
            add(CsBase("기본응대 - 3","긍정 에너지를 전파하는 입점인사",null,"10분 전"))
            add(CsBase("상황응대 - 3","제품에 불만이 있는 고객을 대할 때",null,"2일 전"))
            add(CsBase("기본응대 - 4","긍정 에너지를 전파하는 입점인사",null,"10분 전"))
            add(CsBase("상황응대 - 4","제품에 불만이 있는 고객을 대할 때",null,"2일 전"))
            add(CsBase("기본응대 - 5","긍정 에너지를 전파하는 입점인사",null,"10분 전"))
            add(CsBase("상황응대 - 5","제품에 불만이 있는 고객을 대할 때",null,"2일 전"))
            add(CsBase("기본응대 - 6","긍정 에너지를 전파하는 입점인사",null,"10분 전"))
            add(CsBase("상황응대 - 6","제품에 불만이 있는 고객을 대할 때",null,"2일 전"))
        }
    }

    private fun setRecyclerView() {
        homeBaseCsAdapter = HomeBaseCsAdapter()
        homeBaseCsAdapter.csBaseList = dummyData
        binding.rvHomeBaseCs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeBaseCs.adapter = homeBaseCsAdapter
    }

    private fun setBinding() {
        binding.apply {
            tvBaseMore
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    listener.showCsFragment()
                },{ it.printStackTrace() })
        }
    }

    fun setShowCsFragmentListener(listener: ShowFragmentListener) {
        this.listener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CsBaseFragment().apply {
                arguments = Bundle().apply {
                    putString("ARG_PARAM1", param1)
                    putString("ARG_PARAM2", param2)
                }
            }
    }
}