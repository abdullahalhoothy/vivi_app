package com.app.vivi.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // Prevent the ViewPager from intercepting touch events when scrolling horizontally
        if (ev?.action == MotionEvent.ACTION_MOVE) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        // Prevent the ViewPager from intercepting touch events when scrolling horizontally
        if (ev?.action == MotionEvent.ACTION_MOVE) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(ev)
    }
}
