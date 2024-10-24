package com.app.vivi.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomRatingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var rating = 0f
    private var starSize = 50f // Default star size
    private val starPaint = Paint()

    init {
        starPaint.color = resources.getColor(android.R.color.holo_orange_dark, null) // Change to your desired color
        // You can add custom attributes for starSize and color if needed
    }

    fun setRating(newRating: Float) {
        rating = newRating
        invalidate() // Redraw the view
    }

    fun setStarSize(size: Float) {
        starSize = size
        invalidate() // Redraw the view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw stars
        for (i in 0 until 5) { // 5 stars
            val x = (i * (starSize + 10)) // 10 is the space between stars
            val y = height / 2f

            if (i < rating) {
                canvas.drawStar(x, y, starSize, starPaint) // Draw filled star
            } else {
                starPaint.alpha = 100 // Semi-transparent for empty stars
                canvas.drawStar(x, y, starSize, starPaint) // Draw empty star
                starPaint.alpha = 255 // Reset alpha
            }
        }
    }

    private fun Canvas.drawStar(x: Float, y: Float, size: Float, paint: Paint) {
        val path = android.graphics.Path()
        val halfSize = size / 2

        // Create a star shape
        path.moveTo(x, y - size)
        path.lineTo(x + halfSize * 0.3f, y - halfSize * 0.3f)
        path.lineTo(x + size, y - halfSize * 0.3f)
        path.lineTo(x + halfSize * 0.5f, y + halfSize * 0.3f)
        path.lineTo(x + halfSize * 0.6f, y + size)
        path.lineTo(x, y + halfSize * 0.5f)
        path.lineTo(x - halfSize * 0.6f, y + size)
        path.lineTo(x - halfSize * 0.5f, y + halfSize * 0.3f)
        path.lineTo(x - size, y - halfSize * 0.3f)
        path.lineTo(x - halfSize * 0.3f, y - halfSize * 0.3f)
        path.close()

        this.drawPath(path, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = (5 * (starSize + 10)).toInt() // 5 stars + spacing
        val height = starSize.toInt()
        setMeasuredDimension(width, height)
    }
}
