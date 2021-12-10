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
                            when {
                                !Constants.validateEmail(emailResult.toString()) && emailResult.isNotEmpty() -> {
                                    Timber.e("validateEmail : ${Constants.validateEmail(emailResult.toString())}")
                                    tvErrorEmail.visibility = View.VISIBLE
                                    return@Function3 true
                                }
                                else -> {
                                    tvErrorEmail.visibility = View.GONE
                                    return@Function3 nameResult.isBlank() || emailResult.isBlank() || codeResult.isBlank()
                                            || !Constants.validateEmail(emailResult.toString()) || codeResult.length != 6
                                }
                            }
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
                    focusChangeTitle(it, tvNameTitle)
                }, { it.printStackTrace() })
            etEmailInput
                .focusChanges()
                .subscribe({
                    focusChangeTitle(it, tvEmailTitle)
                }, { it.printStackTrace() })
            etCodeInput
                .focusChanges()
                .subscribe({
                    focusChangeTitle(it, tvCodeTitle)
                }, { it.printStackTrace() })
            etIdInput
                .focusChanges()
                .subscribe({
                    focusChangeTitle(it, tvIdTitle)
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

                        }
                        else -> {}
                    }
                }, { it.printStackTrace() })
        }
    }

    private fun focusChangeTitle(isFocus: Boolean, titleView: TextView) {
        if (isFocus) {
            titleView.visibility = View.VISIBLE
        } else {
            titleView.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "JoinFragment"
    }
}