package com.sokind.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView

class StickyScrollView : NestedScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var header: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f
                it.setOnClickListener { _ ->
                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
                    this.smoothScrollTo(scrollX, it.top)
                    callStickListener()
                }
            }
        }
    var header2: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f
                it.setOnClickListener { _ ->
                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
                    this.smoothScrollTo(scrollX, it.top)
                    callStickListener()
                }
            }
        }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    private var mIsHeaderSticky = false
    private var mIsHeaderSticky2 = false

    private var mHeaderInitPosition = 0f
    private var mHeaderInitPosition2 = 0f
    private var mHeaderHeight = 0f

    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
        mHeaderInitPosition2 = header2?.top?.toFloat() ?: 0f
        mHeaderHeight = header2?.height?.toFloat() ?: 0f
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val scrolly = t

        if (scrolly > mHeaderInitPosition) {
            stickHeader(header,scrolly - mHeaderInitPosition)
        } else {
            freeHeader(header)
        }
        if (header2 != null) {
            if (scrolly > mHeaderInitPosition2) {
                freeHeader(header)
                stickHeader(header2,scrolly - mHeaderInitPosition2)
            } else {
                freeHeader(header2)
            }
        }
    }

    private fun stickHeader(header: View?, position: Float) {
        header?.translationY = position
        callStickListener()
    }

    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
            mIsHeaderSticky2 = false
        }
        if (!mIsHeaderSticky2) {
            stickListener(header2 ?: return)
            mIsHeaderSticky = false
            mIsHeaderSticky2 = true
        }
    }

    private fun freeHeader(header: View?) {
        header?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
        if (mIsHeaderSticky2) {
            freeListener(header2 ?: return)
            mIsHeaderSticky2 = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

}