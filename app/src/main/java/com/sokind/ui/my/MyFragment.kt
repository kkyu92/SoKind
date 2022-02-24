package com.sokind.ui.my

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.member.MemberInfo
import com.sokind.databinding.FragmentMyBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.ui.my.info.InfoActivity
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding>(R.layout.fragment_my) {
    private val viewModel by viewModels<MyViewModel>()
    private lateinit var user: MemberInfo

    private val startForResultInfo =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                viewModel.getMe()
            }
        }

    override fun init() {
        setBinding()

        viewModel.info.observe(viewLifecycleOwner, {
            user = it
            setData()
        })
    }

    private fun setData() {
        binding.apply {
            GlideApp.with(requireContext())
                .load(user.profileImg)
                .error(R.drawable.icon_profile_default)
                .into(ivProfile)
            R.string.name
            tvCsUserName.text = getString(R.string.name_form, user.memberName, user.positionName)
            tvCsUserEnterprise.text = getString(R.string.enterprise_form, user.enterpriseName, user.storeName)
        }
    }

    private fun setBinding() {
        binding.apply {
            llMyInfo
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    startView(InfoActivity::class.java)
                },{ it.printStackTrace() })

            llMyCertification
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
//                    startView()
                },{ it.printStackTrace() })

            llMyNotice
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
//                    startView()
                },{ it.printStackTrace() })

            llMyQuestion
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
//                    startView()
                },{ it.printStackTrace() })

            clMyUpdate
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({

                },{ it.printStackTrace() })
        }
    }

    private fun startView(className: Class<*>) {
        val intent = Intent(requireContext(), className)
        intent.putExtra("info", user)
        startForResultInfo.launch(intent)
    }
}