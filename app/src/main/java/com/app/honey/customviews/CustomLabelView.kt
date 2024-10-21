package com.app.honey.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.app.honey.R

class CustomLabelView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null, 
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val labelTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_label, this, true)
        labelTextView = findViewById(R.id.label_text)
    }

    fun setLabelText(text: String) {
        labelTextView.text = text
    }

    fun setLabelTextColor(color: Int) {
        labelTextView.setTextColor(color)
    }
}
