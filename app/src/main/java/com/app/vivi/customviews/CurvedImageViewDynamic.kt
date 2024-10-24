package com.app.vivi.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.app.vivi.R

class CurvedImageViewDynamic @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val path = Path()
    private var curveFactorX = 2.8f
    private var curveFactorY = 80f
    private var curveFactorY2 = 0.7f

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CurvedImageViewDynamic,
            0, 0
        ).apply {
            try {
                curveFactorX = getDimension(R.styleable.CurvedImageViewDynamic_curveFactorX, 2.8f)
                curveFactorY = getDimension(R.styleable.CurvedImageViewDynamic_curveFactorY, 80f)
                curveFactorY2 = getDimension(R.styleable.CurvedImageViewDynamic_curveFactorY2, 0.7f)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()

        // Reset the path for each draw
        path.reset()

        // Define the curve path
        path.moveTo(0f, 0f) // Top-left corner
        path.lineTo(0f, height * 0.8f) // Left side
        path.quadTo(width / curveFactorX, height + curveFactorY, width, height * curveFactorY2) // Curve from left to right
        path.lineTo(width, 0f) // Right side
        path.close() // Completes the path

        // Clip the canvas to the curved path
        canvas.clipPath(path)

        // Draw the image within the clipped canvas
        super.onDraw(canvas)
    }
}
