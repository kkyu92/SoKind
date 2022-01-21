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
import com.sokind.data.remote.edu.CsBase
import com.sokind.databinding.FragmentCsBinding
import com.sokind.ui.EduNavActivity
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.cs.tabs.CsBaseFragment
import com.sokind.ui.cs.tabs.CsDeepFragment
import com.sokind.util.Constants
import com.sokind.util.ShowReportFragmentListener
import com.sokind.util.adapter.TabAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CsFragment : BaseFragment<FragmentCsBinding>(R.layout.fragment_cs) {
    private val viewModel by viewModels<CsViewModel>()

    private lateinit var showReportFragmentListener: ShowReportFragmentListener
    private lateinit var tabLayoutMediator: TabLayoutMediator

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val go = it.data?.getStringExtra(Constants.MOVE_TO)
            Timber.e("data: ${ it.data}")
            Timber.e("go to : $go")
            when(go) {
                "cs" -> showToast("reload")
                "report" -> showReportFragmentListener.showReportFragment()
            }
        }
    }

    override fun init() {
        setBinding()
        setViewModel()
        // refresh

    }

    private fun setViewModel() {
        viewModel.apply {
            eduList.observe(viewLifecycleOwner, {
                val csBaseFragment = CsBaseFragment(it.baseCs)
                val csDeepFragment = CsDeepFragment(it.deepCs)
                val fragmentList = arrayListOf<Fragment>(
                    csBaseFragment,
                    csDeepFragment
                )
                setTabLayout(fragmentList)
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
            cvCsContinue
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(requireContext(), EduNavActivity::class.java)
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