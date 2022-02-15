package com.sokind.ui.home

import android.app.Activity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.BaseEdu
import com.sokind.data.remote.edu.DeepEdu
import com.sokind.data.remote.member.MemberInfo
import com.sokind.databinding.FragmentHomeBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.OnEduItemClickListener
import com.sokind.util.ShowCsFragmentListener
import com.sokind.util.ShowReportFragmentListener
import com.sokind.util.adapter.BaseEduAdapter
import com.sokind.util.adapter.DeepEduAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var showCsListener: ShowCsFragmentListener
    private lateinit var showReportFragmentListener: ShowReportFragmentListener
    private lateinit var baseEduAdapter: BaseEduAdapter
    private lateinit var deepEduAdapter: DeepEduAdapter

    private lateinit var memberInfo: MemberInfo

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val go = it.data?.getStringExtra(Constants.MOVE_TO)
                Timber.e("data: ${it.data}")
                Timber.e("go to : $go")
                when (go) {
                    "list" -> viewModel.getEdu()
                    "report" -> {
                        viewModel.getEdu()
                        showReportFragmentListener.showReportFragment()
                    }
                }
            }
        }

    override fun init() {
        if (this::showCsListener.isInitialized) {
            setBinding()
            setViewModel()
        }
    }

    private fun setRecyclerView(baseList: List<BaseEdu>, deepList: List<DeepEdu>) {
        baseEduAdapter = BaseEduAdapter("Home")
        baseEduAdapter.baseList = baseList
        deepEduAdapter = DeepEduAdapter("Home")
        deepEduAdapter.deepList = deepList
        binding.apply {
            rvBase.layoutManager = LinearLayoutManager(requireContext())
            rvBase.adapter = baseEduAdapter
            rvDeep.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvDeep.adapter = deepEduAdapter

            if (baseList.isEmpty()) {
                llBaseNo.visibility = View.VISIBLE
                rvBase.visibility = View.GONE
            } else {
                llBaseNo.visibility = View.GONE
                if (baseList.size <= 5) {
                    tvBaseMore.visibility = View.GONE
                } else {
                    tvBaseMore.visibility = View.VISIBLE
                }
            }
            if (deepList.size <= 5) {
                tvDeepMore.visibility = View.GONE
            } else {
                tvDeepMore.visibility = View.VISIBLE
            }

            for (base in baseList) {
                if (base.status == 2) {
                    llDeepNo.visibility = View.VISIBLE
                    rvDeep.visibility = View.GONE
                    tvDeepMore.visibility = View.GONE
                    break
                }
            }
        }
        baseEduAdapter.setOnItemClickListener(object : OnEduItemClickListener {
            override fun onBaseItemClick(edu: BaseEdu, pos: Int) {
                startEdu(edu, startForResult)
            }

            override fun onDeepItemClick(edu: DeepEdu, pos: Int) {
                startEdu(edu, startForResult)
            }
        })
        deepEduAdapter.setOnItemClickListener(object : OnEduItemClickListener {
            override fun onBaseItemClick(edu: BaseEdu, pos: Int) {
                startEdu(edu, startForResult)
            }

            override fun onDeepItemClick(edu: DeepEdu, pos: Int) {
                startEdu(edu, startForResult)
            }
        })
    }

    private fun setBinding() {
        binding.apply {
            refreshLayout.setOnRefreshListener {
                viewModel.getEdu()
                refreshLayout.isRefreshing = false
            }
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
                saveUser(it)
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
            val member = info.memberName + " " + info.positionName
            val enterInfo = info.enterpriseName + " / " + info.storeName
            tvUserName.text = member + "님!"
            tvHomeUserName.text = member
            tvHomeUserEnterprise.text = enterInfo
        }
    }

    fun setShowCsFragmentListener(listenerCs: ShowCsFragmentListener) {
        this.showCsListener = listenerCs
    }

    fun setShowReportFragmentListener(listener: ShowReportFragmentListener) {
        this.showReportFragmentListener = listener
    }

    companion object
}