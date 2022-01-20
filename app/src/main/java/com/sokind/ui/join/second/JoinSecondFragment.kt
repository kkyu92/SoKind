package com.sokind.ui.join.second

import android.view.View
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
        arguments?.apply {
            mEnterpriseKey = getInt("enterprise_key")
            mStoreKey = getInt("store_key")
            mPositionKey = getInt("position_key")
        }
        setBinding()
        setViewModel()
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
                            errorVisible(
                                tvErrorEmail,
                                (!Constants.validateEmail(emailResult.toString()) && emailResult.isNotEmpty())
                            )
                            return@Function3 nameResult.isBlank() || emailResult.isBlank() || codeResult.isBlank()
                                    || !Constants.validateEmail(emailResult.toString())
                                    || etEmailInput.isEnabled || etCodeInput.isEnabled
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
                    btNext.isEnabled = false
                    if (it.isNotEmpty()) {
                        btIdCheck.isEnabled = Constants.validateId(it.toString())
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
            tvEmailCheck
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    if (Constants.validateEmail(etEmailInput.text.toString())) {
                        hideKeyboard()
                        viewModel.sendEmail(etEmailInput.text.toString())
                    } else {
                        showToast("이메일 주소를 확인해주세요.")
                    }
                }, { it.printStackTrace() })
            tvCodeCheck
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    if (mEmailCode.isNullOrEmpty() || mEmailCode != etCodeInput.text.toString()) {
                        showToast("인증번호를 확인해주세요.")
                    } else {
                        tvCodeCheck.visibility = View.GONE
                        etCodeInput.isEnabled = false
                        etCodeInput.text = etCodeInput.text
                        hideKeyboard()
                    }
                }, { it.printStackTrace() })
            btIdCheck
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    hideKeyboard()
                    viewModel.checkId(etIdInput.text.toString())
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
                            val action =
                                JoinSecondFragmentDirections.actionJoinSecondFragmentToJoinThirdFragment(
                                    mEnterpriseKey,
                                    mStoreKey,
                                    mPositionKey,
                                    memberName = etNameInput.text.toString(),
                                    memberEmail = etEmailInput.text.toString(),
                                    memberId = etIdInput.text.toString()
                                )
                            findNavController().navigate(action)
                        }
                        else -> {}
                    }
                }, { it.printStackTrace() })
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            emailCode.observe(viewLifecycleOwner, {
                mEmailCode = it
                binding.etEmailInput.isEnabled = false
                binding.tvEmailCheck.visibility = View.GONE
                showToast("인증코드 전송을 완료했습니다.")
                Timber.e("code : $it")
            })
            idCheck.observe(viewLifecycleOwner, { checked ->
                if (checked) {
                    binding.btNext.isEnabled = true
                    showToast("사용가능한 아이디 입니다.")
                } else {
                    showToast("이미 사용중 아이디 입니다.")
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

    companion object {
        fun newInstance() = JoinSecondFragment()
        private var mEnterpriseKey: Int = 0
        private var mStoreKey: Int = 0
        private var mPositionKey: Int = 0
        private var mEmailCode: String? = null
    }
}