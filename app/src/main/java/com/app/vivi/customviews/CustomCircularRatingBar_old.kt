package com.app.vivi.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class CustomCircularRatingBar_old(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var progress = 4.7f
    private val maxProgress = 5.0f
    private val strokeWidth = 20f
    private val smallTickLength = 15f
    private val largeTickLength = 25f
    private val knobRadius = 30f
    private val dragKnobRadius = 40f // Larger radius during drag
    private val dotRadius = 10f // Radius for the four dots
    private val dotCount = 4 // Number of large dots

    private var dragging = false

    private val backgroundCirclePaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = this@CustomCircularRatingBar_old.strokeWidth
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = this@CustomCircularRatingBar_old.strokeWidth
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

        // Draw background circle with a break at the top
        val breakAngle = 10f // Break in degrees at the top
        canvas.drawArc(
            cx - radius, cy - radius, cx + radius, cy + radius,
            -90f + breakAngle / 2, 360 - breakAngle, false, backgroundCirclePaint
        )

        // Draw gradient progress arc
        val sweepAngle = (progress / maxProgress) * (360 - breakAngle)
        progressPaint.shader = SweepGradient(
            cx, cy,
            intArrayOf(Color.YELLOW, Color.parseColor("#FFA500"), Color.RED),
            floatArrayOf(0f, 0.7f, 1f)
        )
        canvas.save()
        canvas.translate(cx, cy)
        canvas.drawArc(
            -radius, -radius, radius, radius,
            -90f + breakAngle / 2, sweepAngle, false, progressPaint
        )
        canvas.restore()

        // Draw four evenly spaced dots around the circular line
        drawDots(canvas, cx, cy, radius)

        // Draw knob based on drag state
        val angleRad = Math.toRadians((sweepAngle - 90 + breakAngle / 2).toDouble())
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
        drawRatingText(canvas, cx, cy - radius - knobRadius * 1.5f)
    }

    private fun drawDots(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val angleIncrement = (360f - 10f) / dotCount // evenly space dots around circle, considering break at top
        for (i in 0 until dotCount) {
            val angle = -90f + 5f + i * angleIncrement // Start slightly offset to account for break
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

    private fun drawStarInsideKnob(canvas: Canvas, x: Float, y: Float) {
        val starPath = createStarPath(x, y, knobRadius / 2.5f, knobRadius)
        canvas.drawPath(starPath, starPaint)
    }

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

    private fun drawRatingText(canvas: Canvas, cx: Float, cy: Float) {
        textPaint.color = Color.parseColor("#A52A2A") // Dark red for rating text
        canvas.drawText("★ ${"%.1f".format(progress)}", cx, cy, textPaint)
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }

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
}