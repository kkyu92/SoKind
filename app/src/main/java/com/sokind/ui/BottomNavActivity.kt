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
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

@AndroidEntryPoint
class BottomNavActivity : BaseActivity<ActivityBottomNavBinding>(R.layout.activity_bottom_nav) {
    private val backBtnSubject = PublishSubject.create<Boolean>()

    override fun init() {
        compositeDisposable.add(
            backBtnSubject
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    showToast("'뒤로' 버튼을 한 번 더 누르시면 종료됩니다.")
                }
                .timeInterval(TimeUnit.MILLISECONDS)
                .skip(1)
                .filter { interval ->
                    Timber.v("backBtnSubject | interval: $interval")
                    interval.time() < Constants.BACK_BTN_EXIT_TIMEOUT
                }
                .subscribe {
                    finishAffinity()
                    exitProcess(0)
                }
        )

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

    override fun onBackPressed() {
        backBtnSubject.onNext(true)
    }
}