package com.sokind.ui.my.certificate

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.member.MemberInfo
import com.sokind.databinding.ActivityCertificateBinding
import com.sokind.ui.base.BaseActivity
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CertificateActivity :
    BaseActivity<ActivityCertificateBinding>(R.layout.activity_certificate) {
    private val viewModel by viewModels<CertificateViewModel>()
    private lateinit var user: MemberInfo

    override fun init() {
        user = intent.getSerializableExtra("info") as MemberInfo
        viewModel.checkCertificate()
        setBinding()
        setViewModel()
    }

    private fun setBinding() {
        binding.apply {
            content.text = getString(R.string.certificate_title, user.memberName)
            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    onBackPressed()
                }, { it.printStackTrace() })

            certificateBtn
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://marazoa.com/memGraduate?memberId=${user.memberId}")
                    )
                    startActivity(intent)
                }, { it.printStackTrace() })
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            certificateResult.observe(this@CertificateActivity) { isCertificate ->
                if (isCertificate) {
                    binding.noGraduate.visibility = View.GONE
                    binding.graduate.visibility = View.VISIBLE
                    binding.webView.loadUrl("https://marazoa.com/memGraduate?memberId=${user.memberId}")
                } else {
                    binding.noGraduate.visibility = View.VISIBLE
                    binding.graduate.visibility = View.GONE
                }
            }
        }
    }
}