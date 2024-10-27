package com.app.vivi.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class CustomCircularRatingBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Progress values
    private var progress = 4.7f
    private val maxProgress = 5.0f

    // Size and styling parameters
    private val strokeWidth = 20f
    private val knobRadius = 40f
    private val dragKnobRadius = 50f
    private val dotRadius = 20f // Adjusted for larger dots
    private val dotCount = 4
    private val longTickLength = 40f // Length of longer tick marks
    private val shortTickLength = 20f // Length of shorter tick marks
    private val largeGapAngle = 55f // Larger gap at the top

    private var dragging = false

    // Paint objects
    private val backgroundCirclePaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = this@CustomCircularRatingBar.strokeWidth
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = this@CustomCircularRatingBar.strokeWidth
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    private val knobPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val starPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 80f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = width / 2f - strokeWidth - dragKnobRadius
        val cx = width / 2f
        val cy = height / 2f

        // Draw background circle with a larger gap at the top
        canvas.drawArc(
            cx - radius, cy - radius, cx + radius, cy + radius,
            -90f + largeGapAngle / 2, 360 - largeGapAngle, false, backgroundCirclePaint
        )

        // Draw gradient progress arc with color transition
        val sweepAngle = (progress / maxProgress) * (360 - largeGapAngle)
        progressPaint.shader = SweepGradient(
            cx, cy,
            intArrayOf(Color.YELLOW, Color.parseColor("#FFA500"), Color.RED),
            floatArrayOf(0f, 0.7f, 1f)
        )
        canvas.save()
        canvas.translate(cx, cy)
        canvas.drawArc(
            -radius, -radius, radius, radius,
            -90f + largeGapAngle / 2, sweepAngle, false, progressPaint
        )
        canvas.restore()

        // Draw the tick marks around the circle
        drawTickMarks(canvas, cx, cy, radius)

        // Draw four larger dots around the circle
        drawDots(canvas, cx, cy, radius)

        // Draw knob based on drag state
        val angleRad = Math.toRadians((sweepAngle - 90 + largeGapAngle / 2).toDouble())
        val knobX = (cx + radius * cos(angleRad)).toFloat()
        val knobY = (cy + radius * sin(angleRad)).toFloat()

        if (dragging) {
            knobPaint.color = Color.parseColor("#B22222") // Dark red for active knob
            canvas.drawCircle(knobX, knobY, dragKnobRadius, knobPaint)
        } else {
            knobPaint.color = Color.YELLOW
            canvas.drawCircle(knobX, knobY, knobRadius, knobPaint)
            drawStarInsideKnob(canvas, knobX, knobY)
        }

        // Draw rating text at the top center with a star symbol
        drawRatingText(canvas, cx, cy - radius - knobRadius * 0.1f)
    }

    // Draw tick marks (small lines) around the circle


    private fun drawTickMarks(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val tickCount = 60 // Total number of ticks (like a clock)
        val angleIncrement = 360f / tickCount
        val shortTickLength = 20f // Length of shorter tick marks
        val longTickLength = 40f // Length of longer tick marks

        for (i in 0 until tickCount) {
            val angle = i * angleIncrement
            if (angle >= -90 + largeGapAngle / 2 && angle <= 270 - largeGapAngle / 2) { // Avoid ticks in the gap
                val angleRad = Math.toRadians(angle.toDouble())

                // Calculate start and end positions for the tick marks
                val startX = (cx + (radius - shortTickLength) * cos(angleRad)).toFloat()
                val startY = (cy + (radius - shortTickLength) * sin(angleRad)).toFloat()
                val endX = (cx + radius * cos(angleRad)).toFloat()
                val endY = (cy + radius * sin(angleRad)).toFloat()

                val tickLength = if (i % 5 == 0) longTickLength else shortTickLength // Long tick every 5th tick

                val tickStartX = (cx + (radius - tickLength) * cos(angleRad)).toFloat()
                val tickStartY = (cy + (radius - tickLength) * sin(angleRad)).toFloat()

                // Draw the tick mark
                val tickPaint = Paint().apply {
                    color = Color.DKGRAY
                    strokeWidth = 4f
                    isAntiAlias = true
                }

                canvas.drawLine(tickStartX, tickStartY, endX, endY, tickPaint)
            }
        }
    }


 private fun drawTickMarks_old(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val tickCount = 60 // Number of tick marks, like a clock
        val angleIncrement = 360f / tickCount

        for (i in 0 until tickCount) {
            val angle = i * angleIncrement
            if (angle >= -90 + largeGapAngle / 2 && angle <= 270 - largeGapAngle / 2) { // Avoid ticks in the gap
                val angleRad = Math.toRadians(angle.toDouble())
                val startX = (cx + (radius - (if (i % 5 == 0) longTickLength else shortTickLength)) * cos(angleRad)).toFloat()
                val startY = (cy + (radius - (if (i % 5 == 0) longTickLength else shortTickLength)) * sin(angleRad)).toFloat()
                val endX = (cx + radius * cos(angleRad)).toFloat()
                val endY = (cy + radius * sin(angleRad)).toFloat()

                val tickPaint = Paint().apply {
                    color = Color.DKGRAY
                    strokeWidth = 4f
                    isAntiAlias = true
                }
                canvas.drawLine(startX, startY, endX, endY, tickPaint)
            }
        }
    }


    // Draw four large dots around the circular line
    private fun drawDots(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val angleIncrement = (360f - largeGapAngle) / dotCount
        for (i in 0 until dotCount) {
            val angle = -90f + largeGapAngle / 2 + i * angleIncrement
            val angleRad = Math.toRadians(angle.toDouble())
            val dotX = (cx + radius * cos(angleRad)).toFloat()
            val dotY = (cy + radius * sin(angleRad)).toFloat()

            val dotPaint = Paint().apply {
                color = Color.parseColor("#FFA500")
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            canvas.drawCircle(dotX, dotY, dotRadius, dotPaint)
        }
    }

    // Draw star inside the knob
    private fun drawStarInsideKnob(canvas: Canvas, x: Float, y: Float) {
        val starPath = createStarPath(x, y, knobRadius / 2.5f, knobRadius)
        canvas.drawPath(starPath, starPaint)
    }

    // Create a star shape for the knob
    private fun createStarPath(cx: Float, cy: Float, innerRadius: Float, outerRadius: Float): Path {
        val path = Path()
        val section = 2.0 * Math.PI / 5
        path.moveTo(
            (cx + outerRadius * cos(0.0)).toFloat(),
            (cy + outerRadius * sin(0.0)).toFloat()
        )
        for (i in 1 until 5) {
            path.lineTo(
                (cx + innerRadius * cos(section * i - section / 2)).toFloat(),
                (cy + innerRadius * sin(section * i - section / 2)).toFloat()
            )
            path.lineTo(
                (cx + outerRadius * cos(section * i)).toFloat(),
                (cy + outerRadius * sin(section * i)).toFloat()
            )
        }
        path.close()
        return path
    }

    // Draw the rating text at the top with a star symbol
    private fun drawRatingText(canvas: Canvas, cx: Float, cy: Float) {
        textPaint.color = Color.parseColor("#A52A2A") // Dark red for rating text
        canvas.drawText("★ ${"%.1f".format(progress)}", cx, cy, textPaint)
    }

    // Set progress value and redraw
    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }

    // Handle touch events for dragging
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                dragging = true
                val dx = event.x - width / 2f
                val dy = event.y - height / 2f
                val angle = (Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())) + 360) % 360
                setProgress((angle / 360 * maxProgress).toFloat())
                return true
            }
            MotionEvent.ACTION_UP -> {
                dragging = false
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }


    fun getRating(): Float {
        return progress
    }
}
