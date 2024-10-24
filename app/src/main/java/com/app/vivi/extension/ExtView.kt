package com.app.vivi.extension

import android.view.View

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
