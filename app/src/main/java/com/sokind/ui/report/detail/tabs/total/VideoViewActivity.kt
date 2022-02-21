package com.sokind.ui.report.detail.tabs.total

import android.annotation.SuppressLint
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.sokind.R
import com.sokind.databinding.ActivityVideoViewBinding
import com.sokind.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VideoViewActivity : BaseActivity<ActivityVideoViewBinding>(R.layout.activity_video_view) {
    private lateinit var exoPlayer: ExoPlayer

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {
        val getStringUrl = intent.getStringExtra("url")
        setBinding(getStringUrl!!)
    }

    private fun setBinding(url: String) {
        exoPlayer = ExoPlayer.Builder(this).build()

        binding.apply {
            playerView.player = exoPlayer
            playerView.keepScreenOn = true
            playerView.controllerShowTimeoutMs = 1500

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            Timber.e("STATE_BUFFERING")
                            showProgress(true, pbLoading)
                        }
                        Player.STATE_ENDED -> {
                            Timber.e("STATE_ENDED")
                        }
                        Player.STATE_IDLE -> {
                            Timber.e("STATE_IDLE")
                        }
                        Player.STATE_READY -> {
                            Timber.e("STATE_READY")
                            showProgress(false, pbLoading)
                        }
                        else -> {
                            Timber.e("ELSE")
                        }
                    }
                }
            })
            val videoSource = Uri.parse(url)
            val mediaItem = MediaItem.fromUri(videoSource)

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.playerView.onPause()
        exoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}