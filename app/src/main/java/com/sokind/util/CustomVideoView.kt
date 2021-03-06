package com.sokind.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

@SuppressLint("ViewConstructor")
class CustomVideoView : VideoView {
    private var mListener: PlayPauseListener? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    fun setPlayPauseListener(listener: PlayPauseListener?) {
        mListener = listener
    }

    override fun pause() {
        super.pause()
        if (mListener != null) {
            mListener!!.onPause()
        }
    }

    override fun start() {
        super.start()
        if (mListener != null) {
            mListener!!.onPlay()
        }
    }

    interface PlayPauseListener {
        fun onPlay()
        fun onPause()
    }
}