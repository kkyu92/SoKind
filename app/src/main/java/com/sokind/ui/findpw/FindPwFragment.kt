package com.sokind.ui.findpw

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FragmentFindPwBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.findid.FindIdViewModel
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FindPwFragment : BaseFragment<FragmentFindPwBinding>(R.layout.fragment_find_pw) {
    private val viewModel by viewModels<FindIdViewModel>()

    override fun init() {
        binding.apply {
            // focusChanges
            etEmailInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvEmailTitle, it)
                },{ it.printStackTrace() })
            etEmailInput
                .textChanges()
                .subscribe({
                    errorVisible(tvErrorEmail, (!Constants.validateEmail(it.toString()) && it.isNotEmpty()))
                    btNext.isEnabled = Constants.validateEmail(it.toString())
                }, { it.printStackTrace() })

            btNext
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    when (btNext.text.toString()) {
                        getString(R.string.next) -> {
                            llContainer1.visibility = View.GONE
                            llContainer2.visibility = View.VISIBLE
                            tvFindPwContent.text = getString(R.string.find_pw_content2)
                            btNext.text = getString(R.string.check)
                            tvContent.text = String.format(
                                getString(R.string.send_email_content),
                                etEmailInput.text.toString()
                            )
                        }
                        getString(R.string.check) -> {
                            findNavController().popBackStack()
                        }
                    }
                }, { it.printStackTrace() })

            ivBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().popBackStack()
                }, { it.printStackTrace() })
        }
    }
}