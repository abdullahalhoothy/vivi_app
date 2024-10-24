package com.app.vivi.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TitleTextView(
    context: Context,
    attributeSet: AttributeSet?
) : AppCompatTextView(context, attributeSet) {

    init {

//        textSize = resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
        typeface = Typeface.SANS_SERIF

    }

}