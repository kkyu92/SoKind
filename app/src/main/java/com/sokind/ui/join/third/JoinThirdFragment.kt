package com.sokind.ui.join.third

import android.provider.CalendarContract
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FragmentJoinThirdBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class JoinThirdFragment : BaseFragment<FragmentJoinThirdBinding>(R.layout.fragment_join_third) {
    private val viewModel by viewModels<JoinThirdViewModel>()

    override fun init() {
        setBinding()

    }

    private fun setBinding() {
        binding.apply {
            compositeDisposable.add(
                Observable
                    .combineLatest(
                        etPwInput.textChanges(),
                        etPwCheckInput.textChanges(),
                        BiFunction { pwResult: CharSequence, pwCheckResult: CharSequence ->
                            errorVisible(tvErrorPw, (!Constants.validatePw(pwResult.toString()) && pwResult.isNotEmpty()))
                            errorVisible(tvErrorPwCheck, (!Constants.validatePw(pwCheckResult.toString()) && pwCheckResult.isNotEmpty() && pwResult.toString() != pwCheckResult.toString()))
                            return@BiFunction pwResult.isBlank() || pwCheckResult.isBlank()
                                    || !Constants.validatePw(pwResult.toString()) || !Constants.validatePw(pwCheckResult.toString())
                                    || pwResult.toString() != pwCheckResult.toString()
                        }
                    )
                    .subscribe({ enable ->
                        btNext.isEnabled = !enable
                    }, { it.printStackTrace() })
            )

            // change focus title
            etPwInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvPwTitle, it)
                }, { it.printStackTrace() })
            etPwCheckInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvPwCheckTitle, it)
                }, { it.printStackTrace() })

            // clicks
            ivBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().popBackStack()
                }, { it.printStackTrace() })
            btNext
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_joinThirdFragment_to_joinFourthFragment)
                }, { it.printStackTrace() })
        }
    }

    companion object {
        private const val TAG = "JoinThirdFragment"
    }
}