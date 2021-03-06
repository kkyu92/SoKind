package com.sokind.ui.edu

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.edu.Edu
import com.sokind.data.remote.edu.StartEdu
import com.sokind.databinding.FragmentEduBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.dialog.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EduFragment : BaseFragment<FragmentEduBinding>(R.layout.fragment_edu) {
    private val viewModel by viewModels<EduViewModel>()
    private lateinit var edu: Edu
    private lateinit var startEdu: StartEdu
    private var camera: Camera? = null
    private var videoCapture: VideoCapture? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private lateinit var name: String
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun init() {
        edu = arguments?.getSerializable("edu") as Edu
        viewModel.startEdu(edu.key, edu.type)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        setBinding()
        setContainerView(INIT)
        checkPermission()

        viewModel.apply {
            user.observe(viewLifecycleOwner, {
                name = it.memberName!!
            })

            getStartEdu.observe(viewLifecycleOwner, {
                binding.apply {
                    startEdu = it
                    if (edu.type == 1) {
                        tvDeepContent.visibility = View.GONE
                        tvQuestion.text = it.contents
                        tvAnswer.text = it.title
                    } else {
                        tvDeepContent.visibility = View.VISIBLE
                        tvDeepContent.text = it.title
                        tvQuestion.text = it.contents
                        tvAnswer.text = "?????? ${name}?????? ??????????"
                    }
                    pbCount.progressMax = (it.runningTime).toFloat()
                    pbCount.progress = 0f
                    tvCount.text =
                        String.format(getString(R.string.edu_count, it.runningTime.toString()))
                }
            })

            updateEdu.observe(viewLifecycleOwner, {
                Timber.e("$it")
                showLoading(false, binding.pbLoading.loadingContainer)
                when (it.analysisResult) {
                    SUCCESS -> {
                        val action =
                            EduFragmentDirections.actionEduFragmentToEduFinishFragment(
                                edu,
                                it,
                                startEdu.position
                            )
                        findNavController().navigate(action)
                    }
                    FAIL -> {
                        val dialog = BottomSheetDialog.newInstance(
                            Constants.ANALYSIS_ERROR_DIALOG,
                            getString(R.string.alert),
                            getString(R.string.dialog_error),
                            itemClick = { okClick ->
                                if (okClick) {
                                    setContainerView(INIT)
                                } else {
                                    findNavController().popBackStack()
                                }
                            }
                        )
                        dialog.show(parentFragmentManager, dialog.tag)
                    }
                }
            })
        }
    }

    private fun setBinding() {
        binding.apply {
            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    requireActivity().finish()
                }, { it.printStackTrace() })
            btStart
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    setContainerView(START)
                }, { it.printStackTrace() })
            btStartRecord
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    setContainerView(START_RECORDING)
                }, { it.printStackTrace() })
            pbCount
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    setContainerView(STOP_RECORDING)
                }, { it.printStackTrace() })
        }
    }

    private fun setContainerView(viewStatus: String) {
        binding.apply {
            when (viewStatus) {
                INIT -> {
                    shadow.visibility = View.VISIBLE
                    llContainerStart.visibility = View.VISIBLE
                    llContainerQuestion.visibility = View.GONE
                    llContainerCount.visibility = View.GONE
                    ivFrame.visibility = View.VISIBLE
                    GlideApp.with(requireContext()).load(R.drawable.loading_stop).into(ivLoading)
                    GlideApp.with(requireContext()).clear(pbLoading.loadingGif)
                    showLoading(false, pbLoading.loadingContainer)
                }
                START -> {
                    llContainerStart.visibility = View.GONE
                    llContainerQuestion.visibility = View.VISIBLE
                    llContainerCount.visibility = View.GONE
                    ivFrame.visibility = View.GONE
                }
                START_RECORDING -> {
                    shadow.visibility = View.GONE
                    llContainerStart.visibility = View.GONE
                    llContainerQuestion.visibility = View.GONE
                    llContainerCount.visibility = View.VISIBLE
                    GlideApp.with(requireContext()).load(R.raw.loading_main).into(ivLoading)
                    startRecording()
                }
                STOP_RECORDING -> {
                    GlideApp.with(requireContext()).load(R.drawable.loading_stop).into(ivLoading)
                    GlideApp.with(requireContext()).load(R.raw.loading_wh)
                        .into(pbLoading.loadingGif)
                    showLoading(true, pbLoading.loadingContainer)
                    stopRecording()
                }
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        videoCapture = VideoCapture.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        val preview: Preview =
            Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build()
        preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                videoCapture
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("RestrictedApi")
    private fun startRecording() {
        // countDown
        val countDown = startEdu.runningTime
        binding.pbCount.setProgressWithAnimation(countDown.toFloat(), (countDown * 1000).toLong())
        compositeDisposable.add(
            Observable
                .interval(1, TimeUnit.SECONDS)
                .take(countDown.toLong())
                .map { count -> count + 1 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding.tvCount.text = String.format(
                        getString(
                            R.string.edu_count,
                            (countDown - it.toInt()).toString()
                        )
                    )
                }, { it.printStackTrace() }, {
                    setContainerView(STOP_RECORDING)
                })
        )
        // videoFile
        val videoFile = File(
            outputDirectory,
            SimpleDateFormat(
                Constants.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".webm"
        )
        val outputOptions = VideoCapture.OutputFileOptions.Builder(videoFile).build()

        videoCapture?.startRecording(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(videoFile)
                    val msg = "Video Saved"
                    Timber.e("$msg $savedUri")

                    val requestBody = videoFile.asRequestBody("video/webm".toMediaTypeOrNull())
                    val putFile =
                        MultipartBody.Part.createFormData("eduFile", videoFile.name, requestBody)
                    viewModel.updateEdu(putFile, edu.key, edu.type)
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    Timber.e(message)
                }
            })
    }

    @SuppressLint("RestrictedApi")
    private fun stopRecording() {
        videoCapture?.stopRecording()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else requireContext().filesDir
    }

    private fun checkPermission() {
        if (isPermission(Constants.PERMISSIONS)) {
            startCamera()
        } else {
            onRequestPermission(
                mPermissionListener,
                Constants.PERMISSIONS,
                binding.eduContainer
            )
        }
    }

    private val mPermissionListener: PermissionListener = object :
        PermissionListener {
        override fun onGranted() {
            showToast("?????? ????????? ????????????.")

            val preference = requireActivity().getPreferences(Context.MODE_PRIVATE)
            preference.edit().putBoolean("isFirstPermissionCheck", true).apply()
            Timber.e(preference.getBoolean("isFirstPermissionCheck", false).toString())

        }

        override fun onDenied() {
            showToast("?????? ????????? ????????? ???????????????.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        const val INIT = "init"
        const val START = "start"
        const val START_RECORDING = "start_recording"
        const val STOP_RECORDING = "stop_recording"
        const val SUCCESS = "SUCCESS"
        const val FAIL = "FAIL"
    }
}