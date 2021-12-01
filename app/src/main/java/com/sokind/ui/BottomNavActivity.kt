package com.sokind.ui

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sokind.R
import com.sokind.databinding.ActivityBottomNavBinding
import com.sokind.ui.base.BaseActivity
import com.sokind.ui.cs.CsFragment
import com.sokind.ui.guide.GuideFragment
import com.sokind.ui.home.HomeFragment
import com.sokind.ui.my.MyFragment
import com.sokind.ui.report.ReportFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavActivity : BaseActivity<ActivityBottomNavBinding>(R.layout.activity_bottom_nav) {
    override fun init() {
        val navController = findNavController(R.id.fragmentContainerView_bottom)
        binding.bottomNav.itemIconTintList = null
        binding.bottomNav.setupWithNavController(navController)

        val homeFragment = HomeFragment()
        val guideFragment = GuideFragment()
        val csFragment = CsFragment()
        val reportFragment = ReportFragment()
        val myFragment = MyFragment()

        underlineSelectedItem(binding.clMain, R.id.homeFragment) //select first item
        makeCurrentFragment(homeFragment)

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> makeCurrentFragment(homeFragment)
                R.id.guideFragment -> makeCurrentFragment(guideFragment)
                R.id.csFragment -> makeCurrentFragment(csFragment)
                R.id.reportFragment -> makeCurrentFragment(reportFragment)
                R.id.myFragment -> makeCurrentFragment(myFragment)
            }
            underlineSelectedItem(binding.clMain, it.itemId)
            true
        }
    }

    private fun underlineSelectedItem(view: View, itemId: Int) {
        val constraintLayout: ConstraintLayout = view as ConstraintLayout
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setHorizontalBias(
            R.id.underline,
            getItemPosition(itemId)
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun getItemPosition(itemId: Int): Float {
        return when (itemId) {
            R.id.homeFragment -> 0.07f
            R.id.guideFragment -> 0.285f
            R.id.csFragment -> 0.5f
            R.id.reportFragment -> 0.715f
            R.id.myFragment -> 0.93f
            else -> 0.07f
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView_bottom, fragment)
            commit()
        }
    }
}