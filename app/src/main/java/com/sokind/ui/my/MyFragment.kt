package com.sokind.ui.my

import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.FragmentMyBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding>(R.layout.fragment_my) {
    private val viewModel by viewModels<MyViewModel>()

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            llMyInfo
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({

                },{ it.printStackTrace() })

            llMyCertification
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({

                },{ it.printStackTrace() })

            llMyNotice
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({

                },{ it.printStackTrace() })

            llMyQuestion
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({

                },{ it.printStackTrace() })

            clMyUpdate
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({

                },{ it.printStackTrace() })
        }
    }
}