package com.app.vivi.customviews

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.app.vivi.R

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var backgroundColor: Int = 0
    private var cornerRadius: Float = 0f
    private var strokeColor: Int = 0
    private var strokeWidth: Int = 0
    private val backgroundDrawable = GradientDrawable()

    init {
        // Get the custom attributes from XML
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0).apply {
            try {
                backgroundColor = getColor(R.styleable.CustomTextView_backgroundColor, 0)
                cornerRadius = getDimension(R.styleable.CustomTextView_cornerRadius, 0f)
                strokeColor = getColor(R.styleable.CustomTextView_strokeColor, 0) // Default stroke color is 0 (transparent)
                strokeWidth = getDimensionPixelSize(R.styleable.CustomTextView_strokeWidth, 0) // Default stroke width is 0
            } finally {
                recycle()
            }
        }

        // Apply the background, corner radius, and stroke
        setupBackground()
    }

    private fun setupBackground() {
        backgroundDrawable.setColor(backgroundColor)
        backgroundDrawable.cornerRadius = cornerRadius

        // Apply stroke if strokeWidth > 0
        if (strokeWidth > 0) {
            backgroundDrawable.setStroke(strokeWidth, strokeColor)
        }

        background = backgroundDrawable
    }

    // Optionally, allow updates to the background color, corner radius, and stroke programmatically
    fun updateBackgroundColor(color: Int) {
        backgroundColor = color
        setupBackground()
    }

    fun updateCornerRadius(radius: Float) {
        cornerRadius = radius
        setupBackground()
    }

    fun updateStroke(strokeColor: Int, strokeWidth: Int) {
        this.strokeColor = strokeColor
        this.strokeWidth = strokeWidth
        setupBackground()
    }
}
