//package com.app.vivi.customviews
//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Paint
//import android.graphics.RectF
//import android.util.AttributeSet
//import android.view.View
//import android.animation.ValueAnimator
//import androidx.core.content.ContextCompat
//import com.app.vivi.R
//
//class CircularProgressView @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : View(context, attrs, defStyleAttr) {
//
//    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = ContextCompat.getColor(context, R.color.red) // Fill color (water)
//        style = Paint.Style.FILL
//    }
//
//    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = ContextCompat.getColor(context, R.color.white) // Stroke color (border)
//        style = Paint.Style.STROKE
//        strokeWidth = 10f // Stroke width around the circle
//    }
//
//    private var progress = 0f // Percentage filled from 0.0 to 1.0
//    private val oval = RectF()
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val diameter = width.coerceAtMost(height)
//        val radius = diameter / 2f
//        val cx = width / 2f
//        val cy = height / 2f
//
//        // Draw the stroke (outer border)
//        canvas.drawCircle(cx, cy, radius, strokePaint)
//
//        // Draw the water-like fill (progress) inside the circle
//        // Adjust to fill from the bottom upwards
//        val fillHeight = progress * (diameter - strokePaint.strokeWidth)
//        val fillRadius = fillHeight / 2f
//        canvas.drawCircle(cx, cy + (diameter / 2 - fillRadius), fillRadius, paint)
//    }
//
//    // Animate the fill (water rising)
//    fun setProgressWithAnimation(targetProgress: Float, duration: Long = 1000) {
//        val animator = ValueAnimator.ofFloat(0f, targetProgress)
//        animator.duration = duration
//        animator.addUpdateListener {
//            progress = it.animatedValue as Float
//            invalidate() // Redraws the view
//        }
//        animator.start()
//    }
//}



package com.app.vivi.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import androidx.core.content.ContextCompat
import com.app.vivi.R

class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.red) // Color of the circular fill
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.white) // Stroke color
        style = Paint.Style.STROKE
        strokeWidth = 10f // Width of the stroke
    }

    private var progress = 0f // Current progress (0 to 1) to represent the percentage
    private val oval = RectF() // Bounds for the circular fill

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Determine the center and radius for the circular progress
        val diameter = width.coerceAtMost(height)
        val radius = diameter / 2f
        val cx = width / 2f
        val cy = height / 2f

        // Define the bounds for the arc inside the stroke
        val padding = strokePaint.strokeWidth / 2
        oval.set(
            cx - radius + padding,
            cy - radius + padding,
            cx + radius - padding,
            cy + radius - padding
        )

        // Draw the stroke (outer border)
        canvas.drawCircle(cx, cy, radius - padding, strokePaint)

        // Draw the circular progress fill as an arc
        val sweepAngle = progress * 360 // Map progress to 0-360 degrees
        canvas.drawArc(oval, -90f, sweepAngle, true, fillPaint)
    }

    // Animate the progress to a target value with a duration
    fun setProgressWithAnimation(targetProgress: Float, duration: Long = 1000) {
        val animator = ValueAnimator.ofFloat(progress, targetProgress)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            progress = animation.animatedValue as Float
            invalidate() // Redraw the view with the updated progress
        }
        animator.start()
    }
}

