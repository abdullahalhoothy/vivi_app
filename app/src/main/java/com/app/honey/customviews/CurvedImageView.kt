package com.app.honey.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CurvedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        // Create a path for the curved bottom
        val width = width.toFloat()
        val height = height.toFloat()
        
        // Reset the path for each draw
        path.reset()

        // Define the curve path
        path.moveTo(0f, 0f) // Top-left corner
        path.lineTo(0f, height * 0.8f) // Left side
        path.quadTo(width / 2.8f, height + 80f, width, height * 0.7f) // Curve from left to right
        path.lineTo(width, 0f) // Right side
        path.close() // Completes the path

        // Clip the canvas to the curved path
        canvas.clipPath(path)

        // Draw the image within the clipped canvas
        super.onDraw(canvas)
    }
}
