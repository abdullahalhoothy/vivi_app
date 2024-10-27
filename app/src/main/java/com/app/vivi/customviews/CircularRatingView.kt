package com.app.vivi.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CircularRatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundCirclePaint = Paint().apply {
        color = Color.parseColor("#E0E0E0")  // Light color for unfilled part
        strokeWidth = 30f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val ratingArcPaint = Paint().apply {
        color = Color.parseColor("#FFD700")  // Start with a lighter yellow color
        strokeWidth = 30f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val knobPaint = Paint().apply {
        color = Color.YELLOW  // Initial knob color for the star
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 48f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var rating = 0f  // Rating value between 0 and 10

    private var knobMoved = false  // Track if the knob has been moved

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundCirclePaint)

        // Draw the rating arc
        val sweepAngle = (rating / 10f) * 360f
        val arcRect = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        updateRatingArcColor(sweepAngle)  // Update arc color dynamically
        canvas.drawArc(arcRect, -90f, sweepAngle, false, ratingArcPaint)

        // Draw the rating in the center
        canvas.drawText(String.format("%.1f", rating), centerX, centerY + textPaint.textSize / 3, textPaint)

        // Draw the knob
        val knobAngle = Math.toRadians((sweepAngle - 90).toDouble())
        val knobX = (centerX + radius * cos(knobAngle)).toFloat()
        val knobY = (centerY + radius * sin(knobAngle)).toFloat()

        if (knobMoved) {
            // Draw yellow dot knob
            knobPaint.color = Color.YELLOW
            canvas.drawCircle(knobX, knobY, 15f, knobPaint)
        } else {
            // Draw initial knob with a star inside
            knobPaint.color = Color.LTGRAY  // Initial color for the knob
            canvas.drawCircle(knobX, knobY, 25f, knobPaint)  // Larger knob size initially
            drawStarInsideKnob(canvas, knobX, knobY)  // Draw star inside the knob
        }
    }

    private fun updateRatingArcColor(sweepAngle: Float) {
        // Change the arc color gradually from light yellow to dark yellow
        val colorTransition = if (sweepAngle < 180) "#FFC107" else "#FF8F00"  // Light to dark yellow
        ratingArcPaint.color = Color.parseColor(colorTransition)
    }

    private fun drawStarInsideKnob(canvas: Canvas, x: Float, y: Float) {
        val starPath = Path()
        val outerRadius = 10f
        val innerRadius = 5f

        for (i in 0..4) {
            val angle = Math.toRadians(i * 72.0 - 90.0)
            val px = (x + outerRadius * cos(angle)).toFloat()
            val py = (y + outerRadius * sin(angle)).toFloat()
            if (i == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)

            val nextAngle = Math.toRadians((i + 1) * 72.0 - 54.0)
            val innerX = (x + innerRadius * cos(nextAngle)).toFloat()
            val innerY = (y + innerRadius * sin(nextAngle)).toFloat()
            starPath.lineTo(innerX, innerY)
        }
        starPath.close()

        val starPaint = Paint().apply {
            color = Color.YELLOW  // Star color
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        canvas.drawPath(starPath, starPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = (w.coerceAtMost(h) / 2f) - backgroundCirclePaint.strokeWidth
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                // Calculate the angle from center to the touch point
                val dx = event.x - centerX
                val dy = event.y - centerY
                val angle = (Math.toDegrees(atan2(dy, dx).toDouble()) + 360) % 360

                // Map the angle to a rating scale (0 to 10)
                rating = (angle.toFloat() / 360f) * 10f
                knobMoved = true  // Update the knob state
                invalidate()  // Redraw the view
            }
        }
        return true
    }

    fun getRating(): Float = rating
}