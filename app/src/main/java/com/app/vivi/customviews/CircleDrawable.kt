package com.app.vivi.customviews

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class CircleDrawable(private var color: Int) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        val radius = bounds.width().coerceAtMost(bounds.height()) / 2f
        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()

        paint.color = color
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    // Set color dynamically
    fun setCircleColor(newColor: Int) {
        color = newColor
        invalidateSelf() // Invalidate the drawable to trigger a redraw
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}
