package com.sokind.ui.findid

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FindIdFragmentBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FindIdFragment : BaseFragment<FindIdFragmentBinding>(R.layout.find_id_fragment) {
    private val viewModel by viewModels<FindIdViewModel>()

    override fun init() {
        val nameWatcher = binding.etNameInput.textChanges()
        val emailWatcher = binding.etEmailInput.textChanges()
        val emailWatcher2 = binding.etEmailInput2.textChanges()
        val codeWatcher = binding.etCodeInput.textChanges()
        compositeDisposable
            .add(
                Observable
                    .combineLatest(
                        nameWatcher,
                        emailWatcher,
                        BiFunction { nameResult: CharSequence, emailResult: CharSequence ->
                            return@BiFunction nameResult.isBlank() || emailResult.isBlank() || !Constants.validateEmail(emailResult.toString())
                        }
                    )
                    .subscribe({ blank ->
                        binding.btNext.isEnabled = !blank
                    }, { it.printStackTrace() })
            )
        compositeDisposable
            .add(
                Observable
                    .combineLatest(
                        emailWatcher2,
                        codeWatcher,
                        BiFunction { emailResult: CharSequence, codeResult: CharSequence ->
                            return@BiFunction emailResult.isBlank() || codeResult.isBlank()
                        }
                    )
                    .subscribe({ blank ->
                        binding.btNext.isEnabled = !blank
                    }, { it.printStackTrace() })
            )

        binding.apply {
            emailWatcher
                .subscribe({
                    if (Constants.validateEmail(it.toString()) || it.isNullOrEmpty()) {
                        tvErrorEmail.visibility = View.GONE
                    } else {
                        tvErrorEmail.visibility = View.VISIBLE
                    }
                },{ it.printStackTrace() })

            btFindPw
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_findIdFragment_to_findPwFragment)
                },{ it.printStackTrace() })

            btNext
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    when {
                        //TODO api 결과값으로 이동처리
                        llContainer1.isVisible -> {
                            llContainer1.visibility = View.GONE
                            llContainer2.visibility = View.VISIBLE
                            llContainer3.visibility = View.GONE
                            btNext.isEnabled = false
                            tvJoinDate.text = String.format(getString(R.string.join_date, "2021.12.06"))
                        }
                        llContainer2.isVisible -> {
                            llContainer1.visibility = View.GONE
                            llContainer2.visibility = View.GONE
                            llContainer3.visibility = View.VISIBLE
                            btFindPw.visibility = View.VISIBLE
                            btNext.text = "로그인하러 가기"
                        }
                        llContainer3.isVisible -> {
                            findNavController().popBackStack()
                        }
                    }
                },{ it.printStackTrace() })

            ivBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().popBackStack()
                },{ it.printStackTrace() })
        }
    }

    companion object {
        private const val TAG = "FindIdFragment"
    }
}