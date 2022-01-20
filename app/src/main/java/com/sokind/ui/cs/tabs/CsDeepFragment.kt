package com.sokind.ui.cs.tabs

import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.edu.CsDeep
import com.sokind.databinding.FragmentCsDeepBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.adapter.DeepCsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CsDeepFragment(
    private val tabName: String
) : BaseFragment<FragmentCsDeepBinding>(R.layout.fragment_cs_deep) {

    private lateinit var deepCsAdapter: DeepCsAdapter
    val dummyData = mutableListOf<CsDeep>()

    override fun init() {
        initializeList()
        setRecyclerView()
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
        deepCsAdapter = DeepCsAdapter(tabName)
        deepCsAdapter.csDeepList = dummyData
        binding.rvHomeDeepCs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeDeepCs.adapter = deepCsAdapter
    }

    companion object {

    }
}