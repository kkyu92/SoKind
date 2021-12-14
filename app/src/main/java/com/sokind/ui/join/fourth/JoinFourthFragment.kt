package com.sokind.ui.join.fourth

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.sokind.R
import com.sokind.databinding.FragmentJoinFourthBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function6
import timber.log.Timber
import java.util.concurrent.TimeUnit

class JoinFourthFragment : BaseFragment<FragmentJoinFourthBinding>(R.layout.fragment_join_fourth) {
    private val viewModel by viewModels<JoinFourthViewModel>()

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            compositeDisposable.add(
                Observable
                    .combineLatest(
                        cbMale.checkedChanges(),
                        cbFemale.checkedChanges(),
                        cbTerms1.checkedChanges(),
                        cbTerms2.checkedChanges(),
                        cbTerms3.checkedChanges(),
                        cbTerms4.checkedChanges(),
                        Function6 { maleChecked: Boolean, femaleChecked: Boolean,
                                    term1: Boolean, term2: Boolean, term3: Boolean, term4: Boolean ->
                            cbTermsAll.isChecked = term1 && term2 && term3 && term4
                            return@Function6 (maleChecked || femaleChecked) && term1 && term2 && term3
                        }
                    )
                    .subscribe({ enable ->
                        btFinish.isEnabled = enable
                    }, { it.printStackTrace() })
            )

            // clicks
            cbTermsAll
                .clicks()
                .subscribe({
                    if (cbTermsAll.isChecked) {
                        cbTerms1.isChecked = true
                        cbTerms2.isChecked = true
                        cbTerms3.isChecked = true
                        cbTerms4.isChecked = true
                    } else {
                        cbTerms1.isChecked = false
                        cbTerms2.isChecked = false
                        cbTerms3.isChecked = false
                        cbTerms4.isChecked = false
                    }
                }, { it.printStackTrace() })
            ivBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().popBackStack()
                }, { it.printStackTrace() })
            btFinish
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_joinFourthFragment_to_loginFragment)
                    showToast("회원가입을 완료했습니다.")
                }, { it.printStackTrace() })
        }
    }

    companion object {
        fun newInstance() = JoinFourthFragment()
    }
}