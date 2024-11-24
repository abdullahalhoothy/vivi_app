package com.app.vivi.extension

import android.view.View
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.view.ViewCompat
import kotlin.random.Random

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun goneViews(vararg views: View) {
    views.forEach {
        it.gone()
    }
}

fun visibleViews(vararg views: View) {
    views.forEach {
        it.visible()
    }
}

// Extension function to darken a color
fun Int.darkenColor(factor: Float): Int {
    val alpha = Color.alpha(this)
    val red = (Color.red(this) * factor).toInt().coerceIn(0, 255)
    val green = (Color.green(this) * factor).toInt().coerceIn(0, 255)
    val blue = (Color.blue(this) * factor).toInt().coerceIn(0, 255)
    return Color.argb(alpha, red, green, blue)
}

// Extension function to apply a random dark gradient to any View
fun View.applyRandomDarkGradient(factor: Float = 0.8f, orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT) {
    val random = Random
    val color1 = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)).darkenColor(factor)
    val color2 = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)).darkenColor(factor)

    val gradientDrawable = GradientDrawable(
        orientation, // Gradient orientation
        intArrayOf(color1, color2) // Gradient colors
    )
    this.background = gradientDrawable

    // Apply gradient as background
//    ViewCompat.setBackgroundTintList(this, gradientDrawable)
}

fun setClickListener(vararg views: View, onClick: (View) -> Unit) {
    views.forEach { it.setOnClickListener(onClick) }
}

