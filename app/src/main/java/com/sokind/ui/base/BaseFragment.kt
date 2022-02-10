package com.sokind.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.LayoutLoadingBinding
import com.sokind.ui.EduNavActivity
import com.sokind.util.Constants
import com.sokind.util.dialog.BottomSheetDialog
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
        requireActivity().window.setSoftInputMode(SOFT_INPUT_ADJUST_PAN)

        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        init()
    }

    override fun onDestroyView() {
        _binding = null
        compositeDisposable.clear()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
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
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    aPermission
                ) != PackageManager.PERMISSION_GRANTED -> {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            aPermission
                        )
                    ) {
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
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    aPermission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Timber.e("권한이 승인된 상태")
                }
                else -> {
                    Timber.e("PERMISSION : ${ContextCompat.checkSelfPermission(
                        requireContext(),
                        aPermission
                    )}")
                }
            }
        }

        if (permissionList.size > 0) {
            val deniedList = arrayOfNulls<String>(permissionList.size)
            for (i in deniedList.indices) {
                deniedList[i] = permissionList[i]
            }
            requestPermissions(
                deniedList,
                Constants.PERMISSIONS_DATA
            )
        } else {
            mListener.onGranted()
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

    protected fun showLoading(isShow: Boolean, loadingView: LayoutLoadingBinding) {
        if (isShow) {
            GlideApp.with(requireContext()).load(R.raw.loading_wh)
                .into(loadingView.loadingGif)
            loadingView.loadingContainer.visibility = View.VISIBLE
        } else {
            GlideApp.with(requireContext()).clear(loadingView.loadingGif)
            loadingView.loadingContainer.visibility = View.GONE
        }
    }

    protected fun hideKeyboard() {
        binding.root.clearFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    protected fun fromHtml(format: String, htmlString: String?): Spanned {
        return Html.fromHtml(String.format(format, htmlString), Html.FROM_HTML_MODE_LEGACY)
    }

    protected fun getColor(color: Int): Int {
        return requireContext().getColor(color)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected fun getDrawable(drawable: Int): Drawable? {
        return requireContext().getDrawable(drawable)
    }

    protected fun titleFocus(textView: TextView, visible: Boolean) {
        if (visible) {
            textView.setTextColor(getColor(R.color.main_color))
        } else {
            textView.setTextColor(getColor(R.color.font_light_gray))
        }
    }

    protected fun errorVisible(textView: TextView, visible: Boolean) {
        if (visible) {
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
    }

    protected fun startEdu(edu: Edu, startForResult: ActivityResultLauncher<Intent>) {
        when (edu.status) {
            1 -> { // 교육완료
                val dialog = BottomSheetDialog.newInstance(
                    getString(R.string.alert),
                    String.format(getString(R.string.alert_edu_fin, edu.title)),
                    itemClick = {
                        if (it) {
                            startEduActivity(edu, startForResult)
                        }
                    }
                )
                dialog.show(parentFragmentManager, dialog.tag)
            }
            2 -> { // 미교육
                startEduActivity(edu, startForResult)
            }
            3 -> { // 진행중

            }
            4 -> { // 분석중
                showToast("분석중입니다.")
            }
            5 -> { // 분석오류
                val dialog = BottomSheetDialog.newInstance(
                    getString(R.string.alert),
                    String.format(getString(R.string.alert_edu_error, edu.title)),
                    itemClick = {
                        if (it) {
                            startEduActivity(edu, startForResult)
                        }
                    }
                )
                dialog.show(parentFragmentManager, dialog.tag)
            }
        }
    }

    private fun startEduActivity(edu: Edu, startForResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(requireContext(), EduNavActivity::class.java)
        intent.putExtra("edu", edu)
        startForResult.launch(intent)
    }

    protected interface PermissionListener {
        fun onGranted()
        fun onDenied()
    }

    companion object {
        private const val TAG = "BaseFragment"
    }
}