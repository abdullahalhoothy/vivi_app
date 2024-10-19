package com.app.honey.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.app.honey.R

class CurvedBottomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    // Background color for the curve (this will be set from XML)
    private var curveBackgroundColor: Int = Color.BLUE

    // List to store bubbles
    private val bubbles = mutableListOf<Bubble>()

    init {
        // Get the custom attributes from XML
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CurvedBottomView,
            0, 0
        ).apply {
            try {
                curveBackgroundColor = getColor(R.styleable.CurvedBottomView_curveBackgroundColor, Color.BLUE)
            } finally {
                recycle()
            }
        }

        // Start the bubble animation
        startBubbleAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Set the paint color to the curve background color
        paint.color = curveBackgroundColor

        // Draw the curved path
        val path = Path()
        val width = width.toFloat()
        val height = height.toFloat()

        path.moveTo(0f, 0f)
        path.lineTo(0f, height - 100f)
        path.quadTo(width / 2, height + 100f, width, height - 100f)
        path.lineTo(width, 0f)
        path.close()

        canvas.drawPath(path, paint)

        // Draw bubbles with a light, transparent version of the curve background color
        for (bubble in bubbles) {
            paint.color = lightenAndSetOpacity(curveBackgroundColor, 0.2f)
            canvas.drawCircle(bubble.x, bubble.y, bubble.radius, paint)
        }
    }

    // Function to lighten the background color and adjust opacity for subtle bubbles
    private fun lightenAndSetOpacity(color: Int, alphaFactor: Float): Int {
        val alpha = (255 * alphaFactor).toInt()  // Apply alpha for transparency
        val factor = 0.8f // Lighten factor (80% lightened)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)

        val lightRed = Math.min((red + (255 - red) * factor).toInt(), 255)
        val lightGreen = Math.min((green + (255 - green) * factor).toInt(), 255)
        val lightBlue = Math.min((blue + (255 - blue) * factor).toInt(), 255)

        return Color.argb(alpha, lightRed, lightGreen, lightBlue)  // Set alpha for transparency
    }

    // Function to start bubble animation
    private fun startBubbleAnimation() {
        val bubbleAnimator = ValueAnimator.ofFloat(0f, height.toFloat())
        bubbleAnimator.duration = 12000
        bubbleAnimator.repeatCount = ValueAnimator.INFINITE
        bubbleAnimator.addUpdateListener {
            updateBubbles()
            invalidate()
        }
        bubbleAnimator.start()
    }

    // Function to update the bubbles' position
    private fun updateBubbles() {
        for (bubble in bubbles) {
            bubble.y -= bubble.speed
            if (bubble.y < 0) {
                bubble.reset(width.toFloat(), height.toFloat())
            }
        }
        if (bubbles.size < 3) {
            bubbles.add(Bubble(width.toFloat(), height.toFloat()))
        }
    }

    private class Bubble(screenWidth: Float, screenHeight: Float) {
        var x: Float = (Math.random() * screenWidth).toFloat()
        var y: Float = screenHeight
        var radius: Float = getRandomSize()
        var speed: Float = (0.5 + Math.random() * 1.0).toFloat()

        fun reset(screenWidth: Float, screenHeight: Float) {
            x = (Math.random() * screenWidth).toFloat()
            y = screenHeight
            radius = getRandomSize()
            speed = (0.5 + Math.random() * 1.0).toFloat()
        }

        companion object {
            fun getRandomSize(): Float {
                return (80 + Math.random() * 40).toFloat()
            }
        }
    }
}

