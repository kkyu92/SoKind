package com.sokind.ui.join.second

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FragmentJoinSecondBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class JoinSecondFragment : BaseFragment<FragmentJoinSecondBinding>(R.layout.fragment_join_second) {
    private val viewModel by viewModels<JoinSecondViewModel>()

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            // name, email, code textChanges
            compositeDisposable.add(
                Observable
                    .combineLatest(
                        etNameInput.textChanges(),
                        etEmailInput.textChanges(),
                        etCodeInput.textChanges(),
                        Function3 { nameResult: CharSequence, emailResult: CharSequence, codeResult: CharSequence ->
                            errorVisible(tvErrorEmail, (!Constants.validateEmail(emailResult.toString()) && emailResult.isNotEmpty()))
                            return@Function3 nameResult.isBlank() || emailResult.isBlank() || codeResult.isBlank()
                                    || !Constants.validateEmail(emailResult.toString()) || codeResult.length != 6
                            }
                    )
                    .subscribe({ blank ->
                        Timber.e("blank : $blank")
                        btNext.isEnabled = !blank
                    }, { it.printStackTrace() })
            )
            // id change text
            etIdInput
                .textChanges()
                .subscribe({
                    if (it.isNotEmpty()) {
                        btNext.isEnabled = Constants.validateId(it.toString())
                        tvErrorId.isVisible = !Constants.validateId(it.toString())
                    }
                }, { it.printStackTrace() })

            // change focus title
            etNameInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvNameTitle, it)
                }, { it.printStackTrace() })
            etEmailInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvEmailTitle, it)
                }, { it.printStackTrace() })
            etCodeInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvCodeTitle, it)
                }, { it.printStackTrace() })
            etIdInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvIdTitle, it)
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
                    btNext.isEnabled = false
                    when (isVisible) {
                        llContainer1.isVisible -> {
                            llContainer1.visibility = View.GONE
                            llContainer2.visibility = View.VISIBLE
                        }
                        llContainer2.isVisible -> {
                            findNavController().navigate(R.id.action_joinSecondFragment_to_joinThirdFragment)
                        }
                        else -> {}
                    }
                }, { it.printStackTrace() })
        }
    }

    companion object {
        private const val TAG = "JoinFragment"
    }
}