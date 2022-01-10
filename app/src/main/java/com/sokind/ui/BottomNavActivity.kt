package com.sokind.ui

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
import com.sokind.util.ShowCsFragmentListener
import com.sokind.util.ShowReportFragmentListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

@AndroidEntryPoint
class BottomNavActivity : BaseActivity<ActivityBottomNavBinding>(R.layout.activity_bottom_nav) {
    private val backBtnSubject = PublishSubject.create<Boolean>()
    private lateinit var homeFragment: HomeFragment
    private lateinit var guideFragment: GuideFragment
    private lateinit var csFragment: CsFragment
    private lateinit var reportFragment: ReportFragment
    private lateinit var myFragment: MyFragment

    override fun init() {
        homeFragment = HomeFragment()
        guideFragment = GuideFragment()
        csFragment = CsFragment()
        reportFragment = ReportFragment()
        myFragment = MyFragment()

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

        underlineSelectedItem(binding.clMain, R.id.homeFragment) //select first item
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_bottom, homeFragment, "home")
            .commitAllowingStateLoss()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> showFragment("home")
                R.id.guideFragment -> showFragment("guide")
                R.id.csFragment -> showFragment("cs")
                R.id.reportFragment -> showFragment("report")
                R.id.myFragment -> showFragment("my")
            }
            underlineSelectedItem(binding.clMain, it.itemId)
            true
        }

        homeFragment.setShowCsFragmentListener(object : ShowCsFragmentListener {
            override fun showCsFragment() {
                underlineSelectedItem(binding.clMain, R.id.csFragment)
                showFragment("cs")
            }
        })
        csFragment.setShowReportFragmentListener(object : ShowReportFragmentListener {
            override fun showReportFragment() {
                underlineSelectedItem(binding.clMain, R.id.reportFragment)
                showFragment("report")
            }
        })
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

    private fun showFragment(tag: String) {
        val fm = supportFragmentManager
        when (tag) {
            "home" -> {
                binding.bottomNav.menu.findItem(R.id.homeFragment).isChecked = true
                if (fm.findFragmentByTag("home") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("home")!!).commit()
                } else {
                    fm.beginTransaction()
                        .add(R.id.fragmentContainerView_bottom, homeFragment, "home").commit()
                }

                if (fm.findFragmentByTag("guide") != null) {
                    fm.beginTransaction().hide(guideFragment).commit()
                }
                if (fm.findFragmentByTag("cs") != null) {
                    fm.beginTransaction().hide(csFragment).commit()
                }
                if (fm.findFragmentByTag("report") != null) {
                    fm.beginTransaction().hide(reportFragment).commit()
                }
                if (fm.findFragmentByTag("my") != null) {
                    fm.beginTransaction().hide(myFragment).commit()
                }
            }
            "guide" -> {
                binding.bottomNav.menu.findItem(R.id.guideFragment).isChecked = true
                if (fm.findFragmentByTag("home") != null) {
                    fm.beginTransaction().hide(homeFragment).commit()
                }

                if (fm.findFragmentByTag("guide") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("guide")!!).commit()
                } else {
                    fm.beginTransaction()
                        .add(R.id.fragmentContainerView_bottom, guideFragment, "guide").commit()
                }

                if (fm.findFragmentByTag("cs") != null) {
                    fm.beginTransaction().hide(csFragment).commit()
                }
                if (fm.findFragmentByTag("report") != null) {
                    fm.beginTransaction().hide(reportFragment).commit()
                }
                if (fm.findFragmentByTag("my") != null) {
                    fm.beginTransaction().hide(myFragment).commit()
                }
            }
            "cs" -> {
                binding.bottomNav.menu.findItem(R.id.csFragment).isChecked = true
                if (fm.findFragmentByTag("home") != null) {
                    fm.beginTransaction().hide(homeFragment).commit()
                }
                if (fm.findFragmentByTag("guide") != null) {
                    fm.beginTransaction().hide(guideFragment).commit()
                }

                if (fm.findFragmentByTag("cs") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("cs")!!).commit()
                } else {
                    fm.beginTransaction().add(R.id.fragmentContainerView_bottom, csFragment, "cs")
                        .commit()
                }

                if (fm.findFragmentByTag("report") != null) {
                    fm.beginTransaction().hide(reportFragment).commit()
                }
                if (fm.findFragmentByTag("my") != null) {
                    fm.beginTransaction().hide(myFragment).commit()
                }
            }
            "report" -> {
                binding.bottomNav.menu.findItem(R.id.reportFragment).isChecked = true
                if (fm.findFragmentByTag("home") != null) {
                    fm.beginTransaction().hide(homeFragment).commit()
                }
                if (fm.findFragmentByTag("guide") != null) {
                    fm.beginTransaction().hide(guideFragment).commit()
                }
                if (fm.findFragmentByTag("cs") != null) {
                    fm.beginTransaction().hide(csFragment).commit()
                }

                if (fm.findFragmentByTag("report") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("report")!!).commit()
                } else {
                    fm.beginTransaction()
                        .add(R.id.fragmentContainerView_bottom, reportFragment, "report").commit()
                }

                if (fm.findFragmentByTag("my") != null) {
                    fm.beginTransaction().hide(myFragment).commit()
                }
            }
            "my" -> {
                binding.bottomNav.menu.findItem(R.id.myFragment).isChecked = true
                if (fm.findFragmentByTag("home") != null) {
                    fm.beginTransaction().hide(homeFragment).commit()
                }
                if (fm.findFragmentByTag("guide") != null) {
                    fm.beginTransaction().hide(guideFragment).commit()
                }
                if (fm.findFragmentByTag("cs") != null) {
                    fm.beginTransaction().hide(csFragment).commit()
                }
                if (fm.findFragmentByTag("report") != null) {
                    fm.beginTransaction().hide(reportFragment).commit()
                }

                if (fm.findFragmentByTag("my") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("my")!!).commit()
                } else {
                    fm.beginTransaction().add(R.id.fragmentContainerView_bottom, myFragment, "my")
                        .commit()
                }
            }
        }
    }

    override fun onBackPressed() {
        backBtnSubject.onNext(true)
    }
}