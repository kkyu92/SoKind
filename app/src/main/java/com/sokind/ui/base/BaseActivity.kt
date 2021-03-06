package com.sokind.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
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
import com.sokind.util.dialog.LottieDialogFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {
    private var _binding: B? = null
    protected val binding get() = _binding!!
    lateinit var lottieDialog: LottieDialogFragment
    protected val compositeDisposable = CompositeDisposable()

    private lateinit var mListener: PermissionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        lottieDialog = LottieDialogFragment.newInstance()
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
                    Timber.e("???????????? ?????? ??? ??????")
                } else {
                    if (isFirstCheck) {
                        // ?????? ???????????? ????????? ??????
                        preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                        Timber.e("????????????")
                    } else {
                        // ?????? ???????????? ??????
                        val snackBar = Snackbar.make(
                            layout,
                            "?????? ????????? ????????? ???????????????. ????????? ???????????? ?????????????????? ???????????????.",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        snackBar.setAction("??????") {
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
                Timber.e("????????? ????????? ??????")
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

    protected fun showProgressDialog() {
        lottieDialog.show(
            supportFragmentManager,
            lottieDialog.tag
        )
    }

    protected fun hideProgressDialog() {
        if (lottieDialog.isAdded) {
            lottieDialog.dismissAllowingStateLoss()
        }
    }

    protected fun errorVisible(textView: TextView, visible: Boolean) {
        if (visible) {
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }

    protected fun hideKeyboard() {
        binding.root.clearFocus()
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    protected interface PermissionListener {
        fun onGranted()
        fun onDenied()
    }
}