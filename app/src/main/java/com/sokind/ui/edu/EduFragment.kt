package com.sokind.ui.edu

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.databinding.FragmentEduBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EduFragment : BaseFragment<FragmentEduBinding>(R.layout.fragment_edu) {
    private var camera: Camera? = null
    private var videoCapture: VideoCapture? = null
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var isRecording = false
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun init() {
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        setBinding()
        setContainerView(INIT)
        checkPermission()
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
                    GlideApp.with(requireContext()).load(R.drawable.loading_stop).into(ivLoading)
                    GlideApp.with(requireContext()).clear(pbLoading.loadingGif)
                    showLoading(false, pbLoading.loadingContainer)
                }
                START -> {
                    llContainerStart.visibility = View.GONE
                    llContainerQuestion.visibility = View.VISIBLE
                    llContainerCount.visibility = View.GONE
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
                    stopRecording()
                    showLoading(true, pbLoading.loadingContainer)
                    findNavController().navigate(R.id.action_eduFragment_to_eduFinishFragment)
                }
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        videoCapture = VideoCapture.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        val preview: Preview =
            Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build()
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
                    showToast("$msg $savedUri")
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
            showToast("모든 권한의 승인완료.")

            val preference = requireActivity().getPreferences(Context.MODE_PRIVATE)
            preference.edit().putBoolean("isFirstPermissionCheck", true).apply()
            Timber.e(preference.getBoolean("isFirstPermissionCheck", false).toString())

        }

        override fun onDenied() {
            showToast("모든 권한의 승인이 필요합니다.")
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
    }
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder()
//                .build()
//                .also { mPreview ->
//                    mPreview.setSurfaceProvider(
//                        binding.cameraPreview.surfaceProvider
//                    )
//                }
//            imageCapture = ImageCapture.Builder().build()
//            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
//            } catch (e: Exception) {
//                Timber.e(e)
//            }
//        }, ContextCompat.getMainExecutor(this))
//    }
//    private fun takePhoto() {
//        val imageCapture = imageCapture ?: return
//        val photoFile = File(
//            outputDirectory,
//            SimpleDateFormat(
//                Constants.FILE_NAME_FORMAT,
//                Locale.getDefault()
//            ).format(System.currentTimeMillis()) + ".jpg"
//        )
//        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOption,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    val savedUri = Uri.fromFile(photoFile)
//                    val msg = "Photo Saved"
//                    showToast("$msg $savedUri")
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Timber.e(exception)
//                }
//            })
//    }
}