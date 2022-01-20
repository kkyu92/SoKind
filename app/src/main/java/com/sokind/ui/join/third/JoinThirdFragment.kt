package com.sokind.ui.join.third

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
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class JoinThirdFragment : BaseFragment<FragmentJoinThirdBinding>(R.layout.fragment_join_third) {
    private val viewModel by viewModels<JoinThirdViewModel>()

    override fun init() {
        arguments?.apply {
            mEnterpriseKey = getInt("enterprise_key")
            mStoreKey = getInt("store_key")
            mPositionKey = getInt("position_key")
            mName = getString("member_name", null)
            mEmail = getString("member_email", null)
            mId = getString("member_id", null)
        }
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
                    val action = JoinThirdFragmentDirections.actionJoinThirdFragmentToJoinFourthFragment(
                        mEnterpriseKey,
                        mStoreKey,
                        mPositionKey,
                        mName,
                        mEmail,
                        mId,
                        memberPw = etPwInput.text.toString()
                    )
                    findNavController().navigate(action)
                }, { it.printStackTrace() })
        }
    }

    companion object {
        fun newInstance() = JoinThirdFragment()
        private var mEnterpriseKey: Int = 0
        private var mStoreKey: Int = 0
        private var mPositionKey: Int = 0
        private lateinit var mName: String
        private lateinit var mEmail: String
        private lateinit var mId: String
    }
}