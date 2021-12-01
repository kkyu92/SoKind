package com.sokind.ui

import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sokind.R
import com.sokind.databinding.ActivityBottomNavBinding
import com.sokind.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavActivity : BaseActivity<ActivityBottomNavBinding>(R.layout.activity_bottom_nav) {
    override fun init() {
        val navController = findNavController(R.id.fragmentContainerView_bottom)
        binding.bottomNav.itemIconTintList = null
        binding.bottomNav.setupWithNavController(navController)

        underlineSelectedItem(binding.clMain, R.id.fragmentHome) //select first item
        binding.bottomNav.setOnItemSelectedListener {
            underlineSelectedItem(binding.clMain, it.itemId)
            true
        }
    }

    private fun underlineSelectedItem(view: View, itemId: Int) {
        val constraintLayout: ConstraintLayout = view as ConstraintLayout
        TransitionManager.beginDelayedTransition(constraintLayout)
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
            R.id.fragmentHome -> 0.07f
            R.id.fragmentGuide -> 0.285f
            R.id.fragmentCs -> 0.5f
            R.id.fragmentReport -> 0.715f
            R.id.fragmentMy -> 0.93f
            else -> 0.07f
        }
    }
}