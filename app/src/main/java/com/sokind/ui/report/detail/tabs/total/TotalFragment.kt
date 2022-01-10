package com.sokind.ui.report.detail.tabs.total

import android.annotation.SuppressLint
import android.net.Uri
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.MediaController
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.FragmentTotalBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.CustomVideoView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TotalFragment : BaseFragment<FragmentTotalBinding>(R.layout.fragment_total) {
    private val viewModel by viewModels<TotalViewModel>()

    override fun init() {
        setBinding()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setBinding() {
        var isStop = true
        var stopPosition = 0
//        var controller = MediaController(requireContext())
        val uri =
            Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")

        binding.apply {
            videoView.setVideoURI(uri)
//            videoView.setMediaController(controller)
//
//            val lp: FrameLayout.LayoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
//            lp.gravity = Gravity.BOTTOM
//            controller.layoutParams = lp
//            (controller.parent as ViewGroup).removeView(controller)
//            frameLayout.addView(controller)
//
            videoView.setOnPreparedListener {
                it.setOnVideoSizeChangedListener { mp, width, height ->
//                    controller = MediaController(requireContext())
//                    videoView.setMediaController(controller)
//                    controller.setAnchorView(videoView)
                }
                videoView.seekTo(5000)
//                controller.hide()
            }
            videoView.setOnTouchListener { v, event ->
                if (!isStop) {
                    stopPosition = videoView.currentPosition
                    videoView.pause()
                    isStop = true
                } else if (isStop) {
                    videoView.seekTo(stopPosition)
                    videoView.start()
                    isStop = false
                }
                Timber.e("touch")
                false
            }
            videoView.setOnCompletionListener {
                showToast("시청완료")
            }
            videoView.setPlayPauseListener(object : CustomVideoView.PlayPauseListener {
                override fun onPlay() {
                    ivShadow.visibility = View.GONE
                }
                override fun onPause() {
                    ivShadow.visibility = View.VISIBLE
                }
            })
        }
    }

    companion object {
        fun newInstance() = TotalFragment()
    }

}