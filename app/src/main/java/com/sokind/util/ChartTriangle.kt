package com.sokind.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import com.sokind.R

class ChartTriangle : AppCompatImageView {
    private var first = 0
    private var second = 0
    private var third = 0

    private var viewWidth = 0
    private var viewHeight = 0

    private var paint: Paint? = null
    private var path: Path? = null

    private var drawableImg: Drawable? = null
    private var lineWidth = 0f

    private val loot = 1.7320508075688772935274463415059f

    constructor (
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        inflateViews(context, attrs)
    }

    constructor (context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflateViews(context, attrs)
    }

    private fun inflateViews(context: Context, attrs: AttributeSet?) {
        @SuppressLint("CustomViewStyleable") val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.chart)
        first = typedArray.getInt(R.styleable.chart_chart_first, first)
        second = typedArray.getInt(R.styleable.chart_chart_second, second)
        third = typedArray.getInt(R.styleable.chart_chart_third, third)
        lineWidth =
            typedArray.getDimensionPixelOffset(R.styleable.chart_chart_line_width, 0).toFloat()
        drawableImg = typedArray.getDrawable(R.styleable.chart_chart_full_image)
        typedArray.recycle()
        setup()
    }

    fun setData(first: Int, second: Int, third: Int) {
        this.first = first
        this.second = second
        this.third = third
        if (isMeasure) {
            calculatePath()
        }
    }

    private fun setup() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.isAntiAlias = true
        path = Path()
    }

    private var isMeasure = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        isMeasure = true
        viewWidth = width
        viewHeight = height
        calculatePath()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFilledPath(canvas, path, drawableImg)
        paint!!.style = Paint.Style.STROKE
        canvas.drawPath(path!!, paint!!)
    }

    protected fun drawFilledPath(c: Canvas, filledPath: Path?, drawable: Drawable?) {
        val save = c.save()
        c.clipPath(filledPath!!)
        drawable!!.setBounds(0, 0, width, height)
        drawable.draw(c)
        c.restoreToCount(save)
    }

    private fun calculatePath() {
        val centerX = viewWidth.toFloat() / 2
        val centerY = viewHeight.toFloat() * 2 / 3
        val y = viewHeight.toFloat() / 3
        if (first != 0) {
            path!!.moveTo(centerX, centerY - centerY / 100 * first - 14) // first
        } else {
            path!!.moveTo(centerX, centerY) // first
        }
        if (second != 0) {
            path!!.lineTo(
                centerX + centerX / 100 * second - Constants.getAddLinearBack(
                    context,
                    second,
                    lineWidth
                ), centerY + y / 100 * second + 14
            ) // second
        } else {
            path!!.lineTo(centerX, centerY) // first
        }
        if (third != 0) {
            path!!.lineTo(
                centerX - centerX / 100 * third + Constants.getAddLinearBack(
                    context,
                    third,
                    lineWidth
                ), centerY + y / 100 * third + 14
            ) // third
        } else {
            path!!.lineTo(centerX, centerY) // first
        }
        if (first != 0) {
            path!!.moveTo(centerX, centerY - centerY / 100 * first)
        } else {
            path!!.moveTo(centerX, centerY) // first
        }
        path!!.close()
        paint!!.alpha = 54
        invalidate()
    }

    private fun measureWidth(measureSpec: Int): Int {
        val result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            viewWidth
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        val result: Int
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        result = if (specMode == MeasureSpec.EXACTLY) {
            specSize
        } else {
            viewHeight
        }
        return result + 2
    }
}