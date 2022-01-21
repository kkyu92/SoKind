package com.sokind.ui.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.CsBase
import com.sokind.data.remote.edu.CsDeep
import com.sokind.data.remote.edu.Edu
import com.sokind.data.remote.member.MemberInfo
import com.sokind.databinding.FragmentHomeBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.ShowCsFragmentListener
import com.sokind.util.adapter.BaseEduAdapter
import com.sokind.util.adapter.DeepEduAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var showCsListener: ShowCsFragmentListener
    private lateinit var baseEduAdapter: BaseEduAdapter
    private lateinit var deepEduAdapter: DeepEduAdapter

    private lateinit var memberInfo: MemberInfo

    override fun init() {
        if (this::showCsListener.isInitialized) {
            setBinding()
            setViewModel()
        }

        // refresh
    }

    private fun setRecyclerView(baseList: List<Edu>, deepList: List<Edu>) {
        baseEduAdapter = BaseEduAdapter("Home")
        baseEduAdapter.baseList = baseList
        deepEduAdapter = DeepEduAdapter("Home")
        deepEduAdapter.deepList = deepList
        binding.apply {
            rvBase.layoutManager = LinearLayoutManager(requireContext())
            rvBase.adapter = baseEduAdapter
            rvDeep.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvDeep.adapter = deepEduAdapter

            if (baseList.size <= 5) {
                tvBaseMore.visibility = View.GONE
            } else {
                tvBaseMore.visibility = View.VISIBLE
            }
            if (deepList.size <= 5) {
                tvDeepMore.visibility = View.GONE
            } else {
                tvDeepMore.visibility = View.VISIBLE
            }
        }
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
            getEdu()

            getMe.observe(viewLifecycleOwner, {
                memberInfo = it
                setData(it)
            })
            eduList.observe(viewLifecycleOwner, {
                setRecyclerView(it.baseCs, it.deepCs)
            })
            isLoading.observe(viewLifecycleOwner, { isLoading ->
                if (isLoading) {
                    showLoading(true, binding.pbLoading)
                } else {
                    showLoading(false, binding.pbLoading)
                }
            })
        }
    }

    private fun setData(info: MemberInfo) {
        binding.apply {
            tvUserName.text = info.memberName + " " + info.positionName + "님!"
            tvHomeUserName.text = info.memberName + " " + info.positionName
            tvHomeUserEnterprise.text = info.enterpriseName + " / " + info.storeName
        }
    }

    fun setShowCsFragmentListener(listenerCs: ShowCsFragmentListener) {
        this.showCsListener = listenerCs
    }

    companion object {

    }
}