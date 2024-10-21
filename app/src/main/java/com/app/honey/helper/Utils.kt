package com.app.honey.helper

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
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
        <body style='margin:0; padding:0; line-height:1;'>
            <div style='margin:0; padding:0;'>
                <img src='ic_star.png' style='vertical-align:middle;'/> 
                <span style='line-height:1;'><b>$rating</b> $ratingDescription</span>
            </div>
        </body>
    </html>
    """
    return htmlContent
}
fun createRatingDescription(context: Context, ratingDescription: String, rating: String): SpannableStringBuilder {
    val starIcon = ContextCompat.getDrawable(context, R.drawable.ic_star)
    // Set bounds for the star icon (optional: you can adjust size here if needed)
    starIcon?.setBounds(0, 0, starIcon.intrinsicWidth, starIcon.intrinsicHeight)

    // Create an ImageSpan for the star icon
    val imageSpan = ImageSpan(starIcon!!, ImageSpan.ALIGN_BOTTOM)

    // Create a SpannableStringBuilder to hold the image and text
    val spannableStringBuilder = SpannableStringBuilder()

    // Add the image (star icon) first
    val starImagePlaceholder = SpannableString(" ") // Placeholder for image
    starImagePlaceholder.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableStringBuilder.append(starImagePlaceholder)

    // Add a margin (space) after the image
    spannableStringBuilder.append(" ") // Add a space for margin (you can control how much)

    // Add the rating text and style it as bold
    val ratingText = SpannableString("$rating ")
    ratingText.setSpan(StyleSpan(Typeface.BOLD), 0, rating.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableStringBuilder.append(ratingText)

    // Add the rating description
    spannableStringBuilder.append(ratingDescription)

    return spannableStringBuilder
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
