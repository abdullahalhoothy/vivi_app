package com.app.vivi.extension

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}