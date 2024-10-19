package com.app.honey.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import com.app.honey.R

fun hasValue(givenString: String?): Boolean {
    return try {
        givenString != null && !givenString.equals(
            "null",
            ignoreCase = true
        ) && !givenString.equals(
            "",
            ignoreCase = true
        )
    } catch (e: Exception) {
        false
    }//try-catch ends
}//hasValue ends


fun ratingDescription(ratingDescription: String, rating: String): String {
    val htmlContent = """
    <html>
        <body>
            <div>
            <img src='ic_star.png' style='vertical-align:middle;'/> 
                <span><b>$rating</b> $ratingDescription</span>
            </div>
        </body>
    </html>
"""
    return htmlContent
}

// Custom ImageGetter to load images from the drawable folder
class ImageGetter(val context: Context, val drawableResId: Int? = null) : Html.ImageGetter {
    override fun getDrawable(source: String): Drawable? {
        // Check if drawableResId is provided
        drawableResId?.let {
            val drawable = context.resources.getDrawable(it, null)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            return drawable
        }
        // Return null or a default drawable if drawableResId is not provided
        return null // Or you can return a default drawable if needed
    }
}
