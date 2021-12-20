package com.sokind.ui.home

import android.view.View.MeasureSpec
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.home.CsBase
import com.sokind.databinding.FragmentHomeBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.home.tabs.CsBaseFragment
import com.sokind.ui.home.tabs.CsDeepFragment
import com.sokind.ui.home.tabs.HomeTabAdapter
import com.sokind.util.Constants
import com.sokind.util.ShowFragmentListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var showListener: ShowFragmentListener
    private lateinit var homeBaseCsAdapter: HomeBaseCsAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    val dummyData = mutableListOf<CsBase>()

    private val csBaseFragment = CsBaseFragment()
    private val csDeepFragment = CsDeepFragment()
    private val fragmentList = arrayListOf<Fragment>(
        csBaseFragment,
        csDeepFragment
    )

    override fun init() {
        initializeList()
        setRecyclerView()
        setTabLayout()
        setBinding()

        // refresh


        // 기본응대 cs 교육 > 더보기
        csBaseFragment.setShowCsFragmentListener(object : ShowFragmentListener {
            override fun showCsFragment() {
                showListener.showCsFragment()
            }
        })
    }

    fun initializeList() { //임의로 데이터 넣어서 만들어봄
        with(dummyData) {
            add(CsBase("기본응대 - 1", "긍정 에너지를 전파하는 입점인사", null, "10분 전"))
            add(CsBase("상황응대 - 1", "제품에 불만이 있는 고객을 대할 때", null, "2일 전"))
            add(CsBase("기본응대 - 2", "긍정 에너지를 전파하는 입점인사", null, "10분 전"))
            add(CsBase("상황응대 - 2", "제품에 불만이 있는 고객을 대할 때", null, "2일 전"))
            add(CsBase("기본응대 - 3", "긍정 에너지를 전파하는 입점인사", null, "10분 전"))
            add(CsBase("상황응대 - 3", "제품에 불만이 있는 고객을 대할 때", null, "2일 전"))
            add(CsBase("기본응대 - 4", "긍정 에너지를 전파하는 입점인사", null, "10분 전"))
            add(CsBase("상황응대 - 4", "제품에 불만이 있는 고객을 대할 때", null, "2일 전"))
            add(CsBase("기본응대 - 5", "긍정 에너지를 전파하는 입점인사", null, "10분 전"))
            add(CsBase("상황응대 - 5", "제품에 불만이 있는 고객을 대할 때", null, "2일 전"))
            add(CsBase("기본응대 - 6", "긍정 에너지를 전파하는 입점인사", null, "10분 전"))
            add(CsBase("상황응대 - 6", "제품에 불만이 있는 고객을 대할 때", null, "2일 전"))
        }
    }

    private fun setRecyclerView() {
        homeBaseCsAdapter = HomeBaseCsAdapter()
        homeBaseCsAdapter.csBaseList = dummyData
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHome.adapter = homeBaseCsAdapter
    }

    private fun setTabLayout() {
        binding.apply {
            vpHome.apply {
                offscreenPageLimit = 2
                adapter = HomeTabAdapter(fragmentList, childFragmentManager, lifecycle)
                isSaveEnabled = false
            }

            tabLayoutMediator = TabLayoutMediator(tlHome, vpHome) { tab, position ->
                if (position == 0) {
                    tab.text = getString(R.string.basic_cs)
                } else {
                    tab.text = getString(R.string.deep_cs)
                }
            }
            tabLayoutMediator.attach()

            // viewpager different height
            vpHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val view =
                        (vpHome[0] as RecyclerView).layoutManager?.findViewByPosition(position)
                    view?.post {
                        val wMeasureSpec =
                            MeasureSpec.makeMeasureSpec(view.width, MeasureSpec.EXACTLY)
                        val hMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                        view.measure(wMeasureSpec, hMeasureSpec)
                        if (vpHome.layoutParams.height != view.measuredHeight) {
                            vpHome.layoutParams =
                                (vpHome.layoutParams).also { lp -> lp.height = view.measuredHeight }
                        }
                    }
                }
            })
        }
    }

    private fun setBinding() {
        binding.apply {
            tvHomeMore
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    showListener.showCsFragment()
                }, { it.printStackTrace() })
        }
    }

    fun setShowCsFragmentListener(listener: ShowFragmentListener) {
        this.showListener = listener
    }

    override fun onDetach() {
        super.onDetach()
        tabLayoutMediator.detach()
    }

    companion object {

    }
}