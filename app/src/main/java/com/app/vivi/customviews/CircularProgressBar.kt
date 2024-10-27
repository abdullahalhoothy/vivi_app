package com.app.vivi.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var progress = 1.0f
    private val maxProgress = 5.0f // Assuming a max rating of 5 for stars

    private val strokeWidth = 20f
    private val knobRadius = 50f // Larger knob size for custom star inside

    private val circlePaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressBar.strokeWidth
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        shader = SweepGradient(0f, 0f, intArrayOf(Color.YELLOW, Color.rgb(255, 215, 0)), null)
        style = Paint.Style.STROKE
        strokeWidth = this@CircularProgressBar.strokeWidth
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    private val knobPaint = Paint().apply {
        color = Color.RED
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

        val radius = width / 2f - strokeWidth - knobRadius
        val cx = width / 2f
        val cy = height / 2f

        // Draw background circle with ticks
        canvas.drawCircle(cx, cy, radius, circlePaint)
        drawTicks(canvas, cx, cy, radius)

        // Draw progress arc
        val sweepAngle = (progress / maxProgress) * 360
        canvas.save()
        canvas.translate(cx, cy)
        canvas.drawArc(
            -radius, -radius, radius, radius,
            -90f, sweepAngle, false, progressPaint
        )
        canvas.restore()

        // Draw custom knob with star
        val angleRad = Math.toRadians((sweepAngle - 90).toDouble())
        val knobX = (cx + radius * cos(angleRad)).toFloat()
        val knobY = (cy + radius * sin(angleRad)).toFloat()
        drawKnobWithStar(canvas, knobX, knobY)

        // Draw progress text at the top center
        canvas.drawText("${"%.1f".format(progress)}", cx, cy - radius - knobRadius * 1.5f, textPaint)
    }

    private fun drawTicks(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val tickCount = 24
        val tickPaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 4f
            isAntiAlias = true
        }
        for (i in 0 until tickCount) {
            val angle = i * (360 / tickCount) - 90
            val angleRad = Math.toRadians(angle.toDouble())
            val startX = (cx + (radius - 10) * cos(angleRad)).toFloat()
            val startY = (cy + (radius - 10) * sin(angleRad)).toFloat()
            val endX = (cx + radius * cos(angleRad)).toFloat()
            val endY = (cy + radius * sin(angleRad)).toFloat()

            canvas.drawLine(startX, startY, endX, endY, tickPaint)
        }
    }

    private fun drawKnobWithStar(canvas: Canvas, x: Float, y: Float) {
        // Draw outer knob circle
        canvas.drawCircle(x, y, knobRadius, knobPaint)

        // Draw star in the center of the knob
        val starPath = createStarPath(x, y, knobRadius / 2.5f, knobRadius / 1.2f)
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

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            val dx = event.x - width / 2f
            val dy = event.y - height / 2f
            val angle = (Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())) + 360) % 360
            setProgress((angle / 360 * maxProgress).toFloat())
            return true
        }
        return super.onTouchEvent(event)
    }
}
