package com.sokind.ui.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.CsBase
import com.sokind.data.remote.edu.CsDeep
import com.sokind.data.remote.member.MemberInfo
import com.sokind.databinding.FragmentHomeBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.ShowCsFragmentListener
import com.sokind.util.adapter.BaseCsAdapter
import com.sokind.util.adapter.DeepCsAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var showCsListener: ShowCsFragmentListener
    private lateinit var baseCsAdapter: BaseCsAdapter
    private lateinit var deepCsAdapter: DeepCsAdapter
    val dummyBase = mutableListOf<CsBase>()
    val dummyDeep = mutableListOf<CsDeep>()

    private lateinit var memberInfo: MemberInfo

    override fun init() {
        Timber.e("init!!")
        initializeList()
        setRecyclerView()
        setViewModel()
        setBinding()

        // refresh
    }

    fun initializeList() { //임의로 데이터 넣어서 만들어봄
        with(dummyBase) {
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
        with(dummyDeep) {
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
        baseCsAdapter = BaseCsAdapter("Home")
        baseCsAdapter.csBaseList = dummyBase
        binding.rvBase.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBase.adapter = baseCsAdapter
        deepCsAdapter = DeepCsAdapter("Home")
        deepCsAdapter.csDeepList = dummyDeep
        binding.rvDeep.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDeep.adapter = deepCsAdapter
    }

    private fun setBinding() {
        binding.apply {
            tvBaseMore
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    showCsListener.showCsFragment()
                }, { it.printStackTrace() })
            tvDeepMore
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    showCsListener.showCsFragment()
                }, { it.printStackTrace() })
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            getMe.observe(viewLifecycleOwner, {
                memberInfo = it
                setData(it)
            })
        }
    }

    private fun setData(memberInfo: MemberInfo) {
        binding.apply {
            tvUserName.text = memberInfo.memberName + " " + memberInfo.positionName + "님!"
            tvHomeUserName.text = memberInfo.memberName + " " + memberInfo.positionName
            tvHomeUserEnterprise.text = memberInfo.enterpriseName + " / " + memberInfo.storeName
        }
    }

    fun setShowCsFragmentListener(listenerCs: ShowCsFragmentListener) {
        this.showCsListener = listenerCs
    }

    companion object {

    }
}