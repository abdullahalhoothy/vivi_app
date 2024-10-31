package com.app.vivi.extension

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.setDrawableWithSize(
    context: Context,
    drawableResId: Int,
    width: Int,
    height: Int,
    padding: Int
) {
    val drawable = ContextCompat.getDrawable(context, drawableResId)?.apply {
        setBounds(0, 0, width, height)
    }
    this.setCompoundDrawables(drawable, null, null, null)
    this.compoundDrawablePadding = padding
}
