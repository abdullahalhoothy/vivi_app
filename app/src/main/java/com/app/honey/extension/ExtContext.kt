package com.app.honey.extension

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}