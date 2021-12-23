package com.sokind.ui.guide.tabs.manual

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.guide.Manual
import com.sokind.databinding.FragmentManualBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualFragment : BaseFragment<FragmentManualBinding>(R.layout.fragment_manual) {
    private val viewModel by viewModels<ManualViewModel>()

    private lateinit var manualList: List<Manual>
    private lateinit var manualAdapter: ManualAdapter
    val dummyData = mutableListOf<Manual>()

    override fun init() {
        initializeList()
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            manualAdapter = ManualAdapter()
            manualAdapter.manualList = dummyData
            rvManual.setHasFixedSize(true)
            rvManual.layoutManager = LinearLayoutManager(requireContext())
            rvManual.adapter = manualAdapter
        }
    }

    fun initializeList() { //임의로 데이터 넣어서 만들어봄
        with(dummyData) {
            add(Manual("기본응대 - 1", "긍정 에너지를 전파하는 입점인사", getString(R.string.dummy), false))
            add(Manual("상황응대 - 1", "제품에 불만이 있는 고객을 대할 때", getString(R.string.dummy), false))
            add(Manual("기본응대 - 2", "긍정 에너지를 전파하는 입점인사", getString(R.string.dummy), false))
            add(Manual("상황응대 - 2", "제품에 불만이 있는 고객을 대할 때", getString(R.string.dummy), false))
            add(Manual("기본응대 - 3", "긍정 에너지를 전파하는 입점인사", getString(R.string.dummy), false))
            add(Manual("상황응대 - 3", "제품에 불만이 있는 고객을 대할 때", getString(R.string.dummy), false))
            add(Manual("기본응대 - 1", "긍정 에너지를 전파하는 입점인사", getString(R.string.dummy), false))
            add(Manual("상황응대 - 1", "제품에 불만이 있는 고객을 대할 때", getString(R.string.dummy), false))
            add(Manual("기본응대 - 2", "긍정 에너지를 전파하는 입점인사", getString(R.string.dummy), false))
            add(Manual("상황응대 - 2", "제품에 불만이 있는 고객을 대할 때", getString(R.string.dummy), false))
        }
    }

    companion object {
        fun newInstance() = ManualFragment()
    }
}