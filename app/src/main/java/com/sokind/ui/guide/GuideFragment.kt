package com.sokind.ui.guide

import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.sokind.R
import com.sokind.databinding.FragmentGuideBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.guide.tabs.base.GuideBaseFragment
import com.sokind.ui.guide.tabs.deep.GuideDeepFragment
import com.sokind.ui.guide.tabs.manual.ManualFragment
import com.sokind.util.adapter.TabAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideFragment : BaseFragment<FragmentGuideBinding>(R.layout.fragment_guide) {
    private val viewModel by viewModels<GuideViewModel>()

    private lateinit var tabLayoutMediator: TabLayoutMediator

    private val manualFragment = ManualFragment()
    private val csBaseFragment = GuideBaseFragment()
    private val csDeepFragment = GuideDeepFragment()
    private val fragmentList = arrayListOf<Fragment>(
        manualFragment,
        csBaseFragment,
        csDeepFragment
    )

    override fun init() {
        setTabLayout()
    }

    private fun setTabLayout() {
        binding.apply {
            vpGuide.apply {
                offscreenPageLimit = 3
                adapter = TabAdapter(fragmentList, childFragmentManager, lifecycle)
                isSaveEnabled = false
            }

            tabLayoutMediator = TabLayoutMediator(tlGuide, vpGuide) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.guide_tab_1)
                    1 -> tab.text = getString(R.string.guide_tab_2)
                    2 -> tab.text = getString(R.string.guide_tab_3)
                    else -> tab.text = getString(R.string.guide_tab_1)
                }
            }
            tabLayoutMediator.attach()

            // viewpager different height
            vpGuide.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val view =
                        (vpGuide[0] as RecyclerView).layoutManager?.findViewByPosition(position)
                    view?.post {
                        val wMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        view.measure(wMeasureSpec, hMeasureSpec)
                        if (vpGuide.layoutParams.height != view.measuredHeight) {
                            vpGuide.layoutParams =
                                (vpGuide.layoutParams).also { lp -> lp.height = view.measuredHeight }
                        }
                    }
                }
            })
        }
    }

    override fun onDetach() {
        super.onDetach()
        tabLayoutMediator.detach()
    }

    companion object {
        fun newInstance() = GuideFragment()
    }
}