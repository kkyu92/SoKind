package com.sokind.ui.report.detail.tabs.total

import android.annotation.SuppressLint
import android.net.Uri
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.SeekBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import com.sokind.R
import com.sokind.databinding.ActivityVideoViewBinding
import com.sokind.ui.base.BaseActivity
import com.sokind.util.Constants.timeFormat
import com.sokind.util.CustomVideoView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VideoViewActivity : BaseActivity<ActivityVideoViewBinding>(R.layout.activity_video_view) {

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {
        val getStringUrl = intent.getStringExtra("url")
//        var controller = MediaController(this)

        binding.apply {
            showLoading(true, binding.pbLoading)
            videoView.setVideoURI(getStringUrl?.toUri())

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        videoView.seekTo(progress)
                        tvVoiceTime.text = timeFormat.format(progress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            videoView.setOnPreparedListener {
                seekBar.progress = 0
                seekBar.max = videoView.duration
                tvVoiceTime.text = timeFormat.format(0)
                tvVoiceTimeMax.text = timeFormat.format(videoView.duration)
                showLoading(false, binding.pbLoading)
            }
            videoView.setOnTouchListener { v, event ->
                if (videoView.isPlaying) {
                    videoView.pause()
                } else {
//                    videoView.seekTo(videoView.currentPosition)
                    videoView.start()
                    object : Thread() {
                        override fun run() {
                            super.run()
                            while (videoView.isPlaying) {
                                runOnUiThread {
                                    seekBar.progress = videoView.currentPosition
                                    Timber.e("currentPosition: ${videoView.currentPosition}")
                                    tvVoiceTime.text =
                                        timeFormat.format(videoView.currentPosition)
                                }
                                SystemClock.sleep(200)
                            }
                        }
                    }.start()
                }
                Timber.e("touch")
                false
            }

            videoView.setOnCompletionListener {
                showToast("시청완료")
                seekBar.progress = 0
                tvVoiceTime.text = timeFormat.format(0)
                ivShadow.visibility = View.VISIBLE
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
}