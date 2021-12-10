package com.sokind.ui

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.FragmentSplashBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {
    override fun init() {
        val logoAnim = AnimationUtils.loadAnimation(context, R.anim.splash_logo)
        val textAnim = AnimationUtils.loadAnimation(context, R.anim.splash_textview)

        binding.apply {
            ivLogo.startAnimation(logoAnim)
            tvSplash.startAnimation(textAnim)

            btDoPermit
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    onRequestPermission(
                        mPermissionListener,
                        Constants.PERMISSIONS,
                        clSplash
                    )
                }, { it.printStackTrace() })

            tvShowPermit
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    showToast("개인정보 처리방침 화면이동")
                    //TODO 개인정보 처리방침 화면이동
                    // startActivity(Intent().setClass(applicationContext, ::class.java))
                }, { it.printStackTrace() })
        }

        logoAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if (isPermission(Constants.PERMISSIONS)) {
                    Completable
                        .timer(Constants.MILLISECONDS, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                        }, {
                            it.printStackTrace()
                        })
                } else {
                    binding.clPermission.visibility = View.VISIBLE
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }
        })
    }

    private val mPermissionListener: PermissionListener = object :
        PermissionListener {
        override fun onGranted() {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            val preference = requireActivity().getPreferences(Context.MODE_PRIVATE)
            preference.edit().putBoolean("isFirstPermissionCheck", true).apply()
            Timber.e(preference.getBoolean("isFirstPermissionCheck", false).toString())
        }

        override fun onDenied() {
            showToast("모든 권한의 승인이 필요합니다.")
        }
    }
}