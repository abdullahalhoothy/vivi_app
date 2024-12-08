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

fun TextView.setDrawableWithSize(
    context: Context,
    drawableResId: Int,
    width: Int,
    height: Int,
    padding: Int,
    tintColor: Int? = null // Optional tint color
) {
    val drawable = ContextCompat.getDrawable(context, drawableResId)?.apply {
        setBounds(0, 0, width, height)
        mutate() // Ensure the drawable is mutable
        tintColor?.let { setTint(it) } // Apply tint if a color is provided
    }
    this.setCompoundDrawables(drawable, null, null, null)
    this.compoundDrawablePadding = padding
}


fun TextView.setCompoundDrawablesTint(color: Int) {
    compoundDrawablesRelative.forEach { drawable ->
        drawable?.mutate()?.setTint(color)
    }
}
