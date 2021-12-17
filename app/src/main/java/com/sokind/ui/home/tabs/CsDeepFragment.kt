package com.sokind.ui.home.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.home.CsBase
import com.sokind.data.remote.home.CsDeep
import com.sokind.databinding.FragmentCsDeepBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.home.HomeBaseCsAdapter
import com.sokind.ui.home.HomeDeepCsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CsDeepFragment : BaseFragment<FragmentCsDeepBinding>(R.layout.fragment_cs_deep) {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var homeDeepCsAdapter: HomeDeepCsAdapter
    val dummyData = mutableListOf<CsDeep>()

    override fun init() {
        initializeList()
        setRecyclerView()

        arguments?.let {
            param1 = it.getString("ARG_PARAM1")
            param2 = it.getString("ARG_PARAM2")
        }
    }

    fun initializeList() { //임의로 데이터 넣어서 만들어봄
        with(dummyData) {
            add(CsDeep("환불 고객이 나타났다!", "환불을 원하는 고객이 불만이 뒤에 뭐라는거냐", R.drawable.img_sample, getColor(R.color.sub_color2)))
            add(CsDeep("직원 서비스에 불만인 손님 응대", "직원의 태도에 불만족한 고객이 뒤에 뭐라는거냐", null, null))
            add(CsDeep("제품 불만 손님 응대", "제품이 마음에 들지 않아요! 이사람은 뒤에 뭐라는거냐", R.drawable.img_sample, getColor(R.color.sub_color2)))
            add(CsDeep("환불 고객이 나타났다!", "환불을 원하는 고객이 불만이 뒤에 뭐라는거냐", R.drawable.img_sample, getColor(R.color.sub_color2)))
            add(CsDeep("직원 서비스에 불만인 손님 응대", "직원의 태도에 불만족한 고객이 뒤에 뭐라는거냐", null, null))
            add(CsDeep("제품 불만 손님 응대", "제품이 마음에 들지 않아요! 이사람은 뒤에 뭐라는거냐", R.drawable.img_sample, getColor(R.color.sub_color2)))
            add(CsDeep("환불 고객이 나타났다!", "환불을 원하는 고객이 불만이 뒤에 뭐라는거냐", R.drawable.img_sample, getColor(R.color.sub_color2)))
            add(CsDeep("직원 서비스에 불만인 손님 응대", "직원의 태도에 불만족한 고객이 뒤에 뭐라는거냐", null, null))
            add(CsDeep("제품 불만 손님 응대", "제품이 마음에 들지 않아요! 이사람은 뒤에 뭐라는거냐", R.drawable.img_sample, getColor(R.color.sub_color2)))
        }
    }

    private fun setRecyclerView() {
        homeDeepCsAdapter = HomeDeepCsAdapter()
        homeDeepCsAdapter.csDeepList = dummyData
        binding.rvHomeDeepCs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeDeepCs.adapter = homeDeepCsAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CsDeepFragment().apply {
                arguments = Bundle().apply {
                    putString("ARG_PARAM1", param1)
                    putString("ARG_PARAM2", param2)
                }
            }
    }
}