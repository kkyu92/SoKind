package com.sokind.ui.login

import android.content.Context
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val viewModel by viewModels<LoginViewModel>()
    private var loginTry = false
    private var loginInfo = false
    private val backBtnSubject = PublishSubject.create<Boolean>()
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backBtnSubject.onNext(true)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun init() {
        setBinding()
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

        viewModel.apply {
            loginResult.observe(viewLifecycleOwner, {
                if (it) {
                    if (onBoardingFinished()) {
                        findNavController().navigate(R.id.action_loginFragment_to_bottomNavActivity)
                    } else {
                        findNavController().navigate(R.id.action_loginFragment_to_boardingFragment)
                    }
                } else {
                    showToast("")
                }
            })
            isLoading.observe(viewLifecycleOwner, { isLoading ->
                if (isLoading) {
                    showLoading(true, binding.pbLoading)
                } else {
                    showLoading(false, binding.pbLoading)
                }
            })
        }
    }

    private fun setBinding() {
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
                        if (Constants.validatePw(it.toString()) || it.isNullOrEmpty()) {
                            tvErrorPw.visibility = View.GONE
                        } else {
                            tvErrorPw.visibility = View.VISIBLE
                        }
                    }
                }, { it.printStackTrace() })

            tvFindId
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_loginFragment_to_findIdFragment)
                }, { it.printStackTrace() })

            tvFindPw
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_loginFragment_to_findPwFragment)
                }, { it.printStackTrace() })

            btLogin
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    if (!loginTry) {
                        loginTry = true
                    }
                    if (loginInfo) {
                        viewModel.doLoginRequest(
                            binding.etIdInput.text.toString(),
                            binding.etPwInput.text.toString()
                        )
                    } else {
                        showToast("아이디와 비밀번호를 확인해주세요.")
                    }
                }, { it.printStackTrace() })

            tvJoin
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_loginFragment_to_joinFirstFragment)
                }, { it.printStackTrace() })
        }
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}