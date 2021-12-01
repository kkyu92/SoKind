package com.sokind.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sokind.util.Constants
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : Fragment() {
    private var _binding: B? = null
    protected val binding get() = _binding!!
    protected val compositeDisposable = CompositeDisposable()

    private lateinit var mListener: PermissionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        init()
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    abstract fun init()

    protected fun onRequestPermission(
        listener: PermissionListener,
        permissions: Array<String>,
        layout: ConstraintLayout
    ) {
        mListener = listener
        val permissionList = arrayListOf<String>()
        val preference = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)

        for (aPermission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    aPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), aPermission)) {
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
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", requireActivity().packageName, null)
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
            ActivityCompat.requestPermissions(requireActivity(), deniedList, Constants.PERMISSIONS_DATA)
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
            val permissionCheck = ContextCompat.checkSelfPermission(requireContext(), permission)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                isPermission = false
            }
        }
        return isPermission
    }

    protected fun showToast(msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    protected fun showLoading(isShow: Boolean, loadingView: View) {
        if (isShow) {
            loadingView.visibility = View.VISIBLE
        } else {
            loadingView.visibility = View.GONE
        }
    }

    protected interface PermissionListener {
        fun onGranted()
        fun onDenied()
    }
}