package com.sokind.ui.login

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FragmentLoginBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val viewModel by viewModels<LoginViewModel>()
    private var loginTry = false
    private var loginInfo = false

    override fun init() {
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)

        val idWatcher = binding.etIdInput.textChanges()
        val pwWatcher = binding.etPwInput.textChanges()

        binding.apply {
            compositeDisposable
                .add(
                    Observable
                        .combineLatest(
                            idWatcher,
                            pwWatcher,
                            BiFunction { idResult: CharSequence, pwResult: CharSequence ->
                                return@BiFunction idResult.isBlank() || pwResult.isBlank()
                            }
                        )
                        .subscribe({ blank ->
                            loginInfo = !blank
                        }, { it.printStackTrace() })
                )

            etPwInput
                .textChanges()
                .subscribe({
                    if (loginTry) {
                        if (Constants.validatePw(it.toString())) {
                            tvErrorPw.visibility = View.GONE
                        } else {
                            tvErrorPw.visibility = View.VISIBLE
                        }
                    }
                }, { it.printStackTrace() })

            btLogin
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    if (!loginTry) {
                        loginTry = true
                    }
                    if (loginInfo) {
                        //TODO api
                        // 결과값으로 비밀번호 에러 표시
                        showToast("api 확인")
                        findNavController().navigate(R.id.action_loginFragment_to_bottomNavActivity)
                    } else {
                        showToast("아이디와 비밀번호를 확인해주세요.")
                    }
                }, { it.printStackTrace() })
        }
    }

    // back 버튼 눌렀을 때
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backBtnSubject.onNext(true)
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}