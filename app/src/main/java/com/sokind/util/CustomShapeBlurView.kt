package com.sokind.util

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.github.mmin18.widget.RealtimeBlurView

class CustomShapeBlurView(context: Context?, attrs: AttributeSet?): RealtimeBlurView(context, attrs) {
    companion object {
        var mPaint = Paint()
        var mRectF = RectF()
    }

    override fun drawBlurredBitmap(canvas: Canvas, blurredBitmap: Bitmap?, overlayColor: Int) {
        if (blurredBitmap != null) {
            mRectF.right = width.toFloat()
            mRectF.bottom = height.toFloat()
            mPaint.reset()
            mPaint.isAntiAlias = true
            val shader = BitmapShader(blurredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val matrix = Matrix()
            val corners = floatArrayOf(
                // radius 보통 80f
                0f, 0f,   // Top left radius in px
                0f, 0f,   // Top right radius in px
                0f, 0f,     // Bottom right radius in px
                0f, 0f      // Bottom left radius in px
            )
            val path = Path()
            path.addRoundRect(mRectF, corners, Path.Direction.CW)

            matrix.postScale(
                mRectF.width() / blurredBitmap.width,
                mRectF.height() / blurredBitmap.height
            )
            shader.setLocalMatrix(matrix)
            mPaint.shader = shader
//            canvas.drawOval(mRectF, mPaint)
            canvas.drawPath(path, mPaint)
            mPaint.reset()
            mPaint.isAntiAlias = true
            mPaint.color = overlayColor
//            canvas.drawOval(mRectF, mPaint)
            canvas.drawPath(path, mPaint)
        }
    }
}