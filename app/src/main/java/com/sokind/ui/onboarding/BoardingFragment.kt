package com.sokind.ui.onboarding

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FragmentBoardingBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.onboarding.screen.FirstScreen
import com.sokind.ui.onboarding.screen.SecondScreen
import com.sokind.ui.onboarding.screen.ThirdScreen
import com.sokind.util.Constants
import com.sokind.util.adapter.TabAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class BoardingFragment : BaseFragment<FragmentBoardingBinding>(R.layout.fragment_boarding) {
    private val viewModel by viewModels<BoardingViewModel>()
    private val backBtnSubject = PublishSubject.create<Boolean>()
    private lateinit var callBack: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backBtnSubject.onNext(true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callBack)
    }

    override fun onDetach() {
        super.onDetach()
        callBack.remove()
    }

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
                    requireActivity().finishActivity(0)
                    requireActivity().finish()
                }
        )

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        val adapter = TabAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.apply {
            vpBoarding.adapter = adapter
            dotIndicator.setViewPager2(vpBoarding)
        }
    }

    companion object {
        private const val TAG = "BoardingFragment"
    }
}