package com.sokind.ui.my.info.change

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.ActivityChangeBinding
import com.sokind.ui.base.BaseActivity
import com.sokind.util.Constants
import com.sokind.util.dialog.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ChangeActivity : BaseActivity<ActivityChangeBinding>(R.layout.activity_change) {
    private val viewModel by viewModels<ChangeViewModel>()
    private var getCode = ""

    override fun init() {
        setBinding()
        setViewBinding()
    }

    private fun setBinding() {
        binding.apply {
            val title = intent.getStringExtra("title")
            tvTitle.text = title

            if (title == "비밀번호 변경") {
                setBindingPw()
                emailContainer.visibility = View.GONE
                pwContainer.visibility = View.VISIBLE
            } else {
                setBindingEmail()
                emailContainer.visibility = View.VISIBLE
                pwContainer.visibility = View.GONE
            }

            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    onBackPressed()
                }, { it.printStackTrace() })
        }
    }

    private fun setViewBinding() {
        viewModel.apply {
            isLottieLoading.observe(this@ChangeActivity, {
                if (it) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            })

            code.observe(this@ChangeActivity, {
                binding.etNewEmail.isEnabled = false
                binding.tvEmailSend.visibility = View.GONE
                showToast("인증코드 전송을 완료했습니다. $it")
                getCode = it
            })

            isPwChange.observe(this@ChangeActivity, { emailChange ->
                if (emailChange) {
                    val dialog = BottomSheetDialog.newInstance(
                        Constants.CHANGE_DIALOG,
                        getString(R.string.alert_pw_title),
                        getString(R.string.alert_pw_content),
                        itemClick = {
                            if (it) {
                                onBackPressed()
                            }
                        }
                    )
                    dialog.show(supportFragmentManager, dialog.tag)
                }
            })

            isEmailChange.observe(this@ChangeActivity, { pwChange ->
                if (pwChange) {
                    val dialog = BottomSheetDialog.newInstance(
                        Constants.CHANGE_DIALOG,
                        getString(R.string.alert_email_title),
                        "",
                        itemClick = {
                            if (it) {
                                onBackPressed()
                            }
                        }
                    )
                    dialog.show(supportFragmentManager, dialog.tag)
                }
            })
        }
    }

    private fun setBindingEmail() {
        binding.apply {
            compositeDisposable.add(
                Observable
                    .combineLatest(
                        etNewEmail.textChanges(),
                        etCode.textChanges(),
                        BiFunction { emailResult: CharSequence, codeResult: CharSequence ->
                            errorVisible(
                                errorEmail,
                                (!Constants.validateEmail(emailResult.toString()) && emailResult.isNotEmpty())
                            )
                            errorVisible(
                                errorCode,
                                (codeResult.toString() != getCode && codeResult.isNotEmpty())
                            )
                            return@BiFunction emailResult.isBlank() || codeResult.isBlank()
                                    || !Constants.validateEmail(emailResult.toString())
                                    || etNewEmail.isEnabled || codeResult.toString() != getCode
                        }
                    )
                    .subscribe({ blank ->
                        btEmail.isEnabled = !blank
                    }, { it.printStackTrace() })
            )

            tvEmailSend
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    if (Constants.validateEmail(etNewEmail.text.toString())) {
                        hideKeyboard()
                        viewModel.sendEmail(etNewEmail.text.toString())
                    } else {
                        showToast("이메일 주소를 확인해주세요.")
                    }
                }, { it.printStackTrace() })

            btEmail
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    viewModel.changeEmail(etNewEmail.text.toString())
                }, { it.printStackTrace() })
        }
    }

    private fun setBindingPw() {
        binding.apply {
            compositeDisposable.add(
                Observable
                    .combineLatest(
                        etPwNow.textChanges(),
                        etPwNew.textChanges(),
                        etPwNewCheck.textChanges(),
                        Function3 { nowPwResult: CharSequence, newPwResult: CharSequence, newPwCheckResult: CharSequence ->
                            errorVisible(
                                errorPw,
                                (!Constants.validatePw(nowPwResult.toString()) && nowPwResult.isNotEmpty())
                            )
                            errorVisible(
                                errorPwNew,
                                (!Constants.validatePw(newPwResult.toString()) && newPwResult.isNotEmpty())
                            )
                            errorVisible(
                                errorPwCheck,
                                (!Constants.validatePw(newPwCheckResult.toString()) && newPwCheckResult.isNotEmpty())
                            )
                            return@Function3 nowPwResult.isBlank() || newPwResult.isBlank() || newPwCheckResult.isBlank()
                                    || !Constants.validateEmail(nowPwResult.toString())
                                    || !Constants.validateEmail(newPwResult.toString())
                                    || !Constants.validateEmail(newPwCheckResult.toString())
                        }
                    )
                    .subscribe({ blank ->
                        btPw.isEnabled = !blank
                    }, { it.printStackTrace() })
            )

            btPw
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    viewModel.changePw(etPwNow.text.toString(), etPwNew.text.toString())
                }, { it.printStackTrace() })
        }
    }

    override fun onBackPressed() {
        if (binding.etNewEmail.text.toString().isNotEmpty()) {
            val intent = Intent().putExtra("newEmail", binding.etNewEmail.text.toString())
            setResult(RESULT_OK, intent)
        }
        super.onBackPressed()
    }
}