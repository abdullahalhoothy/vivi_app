package com.app.vivi.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.app.vivi.R

class CustomCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circleColor: Int = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        // Load custom attributes
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomCircleView,
            0, 0
        )
        try {
            circleColor = typedArray.getColor(
                R.styleable.CustomCircleView_circleColor,
                ContextCompat.getColor(context, android.R.color.holo_blue_light)
            )
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = width.coerceAtMost(height) / 2f
        paint.color = circleColor
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    // Method to dynamically change the color
    fun setCircleColor(color: Int) {
        circleColor = color
        invalidate() // Re-draw the view with the new color
    }
}
