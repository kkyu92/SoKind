package com.sokind.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.databinding.LayoutLoadingBinding
import com.sokind.util.Constants
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {
    private var _binding: B? = null
    protected val binding get() = _binding!!
    protected val compositeDisposable = CompositeDisposable()

    private lateinit var mListener: PermissionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        init()
    }

    override fun onDestroy() {
        _binding = null
        compositeDisposable.dispose()
        super.onDestroy()
    }

    abstract fun init()

    protected fun onRequestPermission(
        listener: PermissionListener,
        permissions: Array<String>,
        layout: ConstraintLayout
    ) {
        mListener = listener
        val permissionList = arrayListOf<String>()
        val preference = getPreferences(Context.MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)

        for (aPermission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    aPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, aPermission)) {
                    Timber.e("승인거절 한번 한 상태")
                } else {
                    if (isFirstCheck) {
                        // 처음 물었는지 여부를 저장
                        preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                        Timber.e("처음여부")
                    } else {
                        // 다시 표시하지 않음
                        val snackBar = Snackbar.make(
                            layout,
                            "다음 권한의 허용이 필요합니다. 확인을 누르시면 설정화면으로 이동합니다.",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        snackBar.setAction("확인") {
                            val intent = Intent()
                            intent.action = ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        snackBar.show()
                        return
                    }
                }
                permissionList.add(aPermission)
                Timber.e("권한이 거절된 상태")
            }
        }

        if (permissionList.size > 0) {
            val deniedList = arrayOfNulls<String>(permissionList.size)
            for (i in deniedList.indices) {
                deniedList[i] = permissionList[i]
            }
            ActivityCompat.requestPermissions(this, deniedList, Constants.PERMISSIONS_DATA)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.PERMISSIONS_DATA -> {
                var i = 0
                while (i < permissions.size) {
                    if (grantResults.isNotEmpty() && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mListener.onDenied()
                        return
                    }
                    i++
                }
                mListener.onGranted()
            }
        }
    }

    protected fun isPermission(permissions: Array<String>): Boolean {
        var isPermission = true
        for (permission in permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                isPermission = false
            }
        }
        return isPermission
    }

    protected fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    protected fun showLoading(isShow: Boolean, loadingView: View) {
        if (isShow) {
            loadingView.visibility = View.VISIBLE
        } else {
            loadingView.visibility = View.GONE
        }
    }

    protected fun showLoading(isShow: Boolean, loadingView: LayoutLoadingBinding) {
        if (isShow) {
            GlideApp.with(this).load(R.raw.loading_wh)
                .into(loadingView.loadingGif)
            loadingView.loadingContainer.visibility = View.VISIBLE
        } else {
            GlideApp.with(this).clear(loadingView.loadingGif)
            loadingView.loadingContainer.visibility = View.GONE
        }
    }

    protected fun showProgress(isShow: Boolean, loadingView: LayoutLoadingBinding) {
        if (isShow) {
            loadingView.loadingContainer.visibility = View.VISIBLE
            loadingView.progressBar.visibility = View.VISIBLE
        } else {
            loadingView.loadingContainer.visibility = View.GONE
            loadingView.progressBar.visibility = View.GONE
        }
    }

    protected interface PermissionListener {
        fun onGranted()
        fun onDenied()
    }
}