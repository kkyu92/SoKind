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
import com.sokind.ui.report.detail.tabs.expression.ExpressionScore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

class HexagonChartView : AppCompatImageView {
    private lateinit var myScore: ExpressionScore
    private var viewWidth = 0
    private var viewHeight = 0
    private var paint = Paint()
    private var path = Path()
    private lateinit var transparentDrawable: Drawable
    private lateinit var strokeDrawable: Drawable
    private var lineWidth = 0f

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inflateViews(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflateViews(context, attrs)
    }

    private fun inflateViews(context: Context, attrs: AttributeSet?) {
        @SuppressLint("CustomViewStyleable") val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.chart)
        lineWidth =
            typedArray.getDimensionPixelOffset(R.styleable.chart_chart_line_width, 0).toFloat()
        transparentDrawable = typedArray.getDrawable(R.styleable.chart_chart_full_image)!!
        strokeDrawable = typedArray.getDrawable(R.styleable.chart_chart_stroke_image)!!
        typedArray.recycle()
        setup()
    }

    fun setData(myScore: ExpressionScore) {
        this.myScore = myScore
        calculatePath()
    }

    private fun setup() {
        paint.color = resources.getColor(R.color.main_color)
        paint.strokeWidth = 3f
        paint.isAntiAlias = true
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        viewWidth = width
        viewHeight = height
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFilledPath(canvas, path, transparentDrawable)
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path, paint)
    }

    private fun drawFilledPath(c: Canvas, filledPath: Path, drawable: Drawable) {
        val save = c.save()
        c.clipPath(filledPath)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(c)
        c.restoreToCount(save)
    }

    private fun calculatePath() {
        Observable.fromCallable {}
            .subscribeOn(Schedulers.io()) // report or post the result to main thread.
            .observeOn(AndroidSchedulers.mainThread()) // execute this RxJava
            .subscribe({
                backgroundTask()
            }, {}, {
                invalidate()
            })
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

    private fun backgroundTask() {
        val centerX = viewWidth / 2.toFloat()
        val centerY = viewHeight / 2.toFloat()
        val percent = centerY / 100
        val secondPoint = centerY / 100 * myScore.glum.toFloat() * loot / 2
        val thirdPoint = centerY / 100 * myScore.excitement.toFloat() * loot / 2
        val fifthPoint = centerY / 100 * myScore.bright.toFloat() * loot / 2
        val sixthPoint = centerY / 100 * myScore.fear.toFloat() * loot / 2
        var y: Float

        // my
        if (myScore.surprise != 0) {
            y = centerY / 100 * myScore.surprise.toFloat()
            path.moveTo(centerX, centerY - y) // first
        } else {
            path.moveTo(centerX, centerY) // first
        }
        if (myScore.glum != 0) {
            path.lineTo(
                centerX + secondPoint - Constants.getAddLinearBack(
                    context,
                    myScore.glum,
                    lineWidth
                ), centerY - percent * myScore.glum / 2
            ) // second
        } else {
            path.lineTo(centerX, centerY) // first
        }
        if (myScore.excitement != 0) {
            path.lineTo(
                centerX + thirdPoint - Constants.getAddLinearBack(
                    context,
                    myScore.excitement,
                    lineWidth
                ), centerY + percent * myScore.excitement / 2
            ) // third
        } else {
            path.lineTo(centerX, centerY) // first
        }
        if (myScore.sympathy != 0) {
            y = centerY / 100 * myScore.sympathy.toFloat()
            path.lineTo(centerX, centerY + y) // fourth
        } else {
            path.lineTo(centerX, centerY) // first
        }
        if (myScore.bright != 0) {
            path.lineTo(
                centerX - fifthPoint + Constants.getAddLinearBack(
                    context,
                    myScore.bright,
                    lineWidth
                ), centerY + percent * myScore.bright / 2
            ) // fifth
        } else {
            path.lineTo(centerX, centerY) // first
        }
        if (myScore.fear != 0) {
            path.lineTo(
                centerX - sixthPoint + Constants.getAddLinearBack(
                    context,
                    myScore.fear,
                    lineWidth
                ), centerY - percent * myScore.fear / 2
            ) // sixth
        } else {
            path.lineTo(centerX, centerY) // first
        }
        if (myScore.surprise != 0) {
            y = centerY / 100 * myScore.surprise.toFloat()
            path.lineTo(centerX, centerY - y) // first
        } else {
            path.lineTo(centerX, centerY) // first
        }
        path.close()
    }

    companion object {
        private val TAG = HexagonChartView::class.java.simpleName
        private const val loot = 1.7320508075688772935274463415059f
    }
}