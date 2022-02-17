package com.sokind.ui.cs

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.BaseEdu
import com.sokind.data.remote.edu.Edu
import com.sokind.data.remote.edu.EduList
import com.sokind.databinding.FragmentCsBinding
import com.sokind.ui.EduNavActivity
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.cs.tabs.CsBaseFragment
import com.sokind.ui.cs.tabs.CsDeepFragment
import com.sokind.util.ChartLv
import com.sokind.util.Constants
import com.sokind.util.ShowReportFragmentListener
import com.sokind.util.adapter.TabAdapter
import com.sokind.util.dialog.BottomSheetExplainDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CsFragment : BaseFragment<FragmentCsBinding>(R.layout.fragment_cs) {
    private val viewModel by viewModels<CsViewModel>()

    private lateinit var showReportFragmentListener: ShowReportFragmentListener
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var baseList: List<BaseEdu>
    private lateinit var nextEduData: Edu

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
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
        setBinding()
        setViewModel()
    }

    private fun setViewModel() {
        viewModel.apply {
            eduList.observe(viewLifecycleOwner, {
                baseList = it.baseCs
                var deepVisible = true
                for (base in it.baseCs) {
                    if (base.status == 2) {
                        deepVisible = false
                        break
                    }
                }
                val csBaseFragment = CsBaseFragment(it.baseCs, startForResult)
                val csDeepFragment = CsDeepFragment(it.deepCs, startForResult, deepVisible)
                val fragmentList = arrayListOf<Fragment>(
                    csBaseFragment,
                    csDeepFragment
                )
                setTabLayout(fragmentList)
                ChartLv(binding.lvChart, it)
                binding.tvCsDay.text = fromHtml(getString(R.string.cs_day), it.eduDate.toString())
            })
            getMe.observe(viewLifecycleOwner, {
                binding.apply {
                    tvCsUserName.text = it.memberName + it.positionName
                    tvCsUserEnterprise.text = it.enterpriseName + " / " + it.storeName
                }
            })
            nextEdu.observe(viewLifecycleOwner, {
                nextEduData = it
                val pos = baseList.indexOf(nextEduData) + 1
                val type = if (it.type == 1) "기본응대 - $pos" else "상황응대 - "
                binding.apply {
                    cvCsContinue.visibility = View.VISIBLE
                    tvCsNext.text = "$type `${it.title}`"
                }
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

    private fun setTabLayout(fragmentList: List<Fragment>) {
        binding.apply {
            vpCs.apply {
                offscreenPageLimit = 2
                adapter = TabAdapter(fragmentList, childFragmentManager, lifecycle)
                isSaveEnabled = false
            }

            tabLayoutMediator = TabLayoutMediator(tlCs, vpCs) { tab, position ->
                if (position == 0) {
                    tab.text = getString(R.string.basic_cs)
                } else {
                    tab.text = getString(R.string.deep_cs)
                }
            }
            tabLayoutMediator.attach()

            // viewpager different height
            vpCs.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val view =
                        (vpCs[0] as RecyclerView).layoutManager?.findViewByPosition(position)
                    view?.post {
                        val wMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                        val hMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        view.measure(wMeasureSpec, hMeasureSpec)
                        if (vpCs.layoutParams.height != view.measuredHeight) {
                            vpCs.layoutParams =
                                (vpCs.layoutParams).also { lp -> lp.height = view.measuredHeight }
                        }
                    }
                }
            })
        }
    }

    private fun setBinding() {
        binding.apply {
            refreshLayout.setOnRefreshListener {
                viewModel.getEdu()
                tlCs.getTabAt(1)!!.select()
                tlCs.getTabAt(0)!!.select()
                refreshLayout.isRefreshing = false
            }

            cvCsContinue
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(requireContext(), EduNavActivity::class.java)
                    intent.putExtra("edu", nextEduData)
                    startForResult.launch(intent)
                }, { it.printStackTrace() })

            svSticky.run {
                header = llHeader
                stickListener = { _ ->
                    Timber.e("stickListener")
                }
                freeListener = { _ ->
                    Timber.e("freeListener")
                }
            }

            ivLvInfo
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetExplainDialog.newInstance(
                        getString(R.string.lv_title),
                        getString(R.string.lv_content),
                        null, null
                    )
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })
        }
    }

    override fun onDetach() {
        super.onDetach()
        tabLayoutMediator.detach()
    }

    fun setShowReportFragmentListener(listener: ShowReportFragmentListener) {
        this.showReportFragmentListener = listener
    }

    companion object {

    }
}