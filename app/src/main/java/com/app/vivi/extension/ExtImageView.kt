import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import com.app.vivi.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

fun ImageView.roundLeftCorners(radius: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val path = Path().apply {
                    // Define the rounded rectangle with only top-left and bottom-left rounded
                    val rect = RectF(0f, 0f, view.width.toFloat(), view.height.toFloat())
                    addRoundRect(
                        rect,
                        floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius),
                        Path.Direction.CW
                    )
                }
                outline.setConvexPath(path)
            }
        }
        clipToOutline = true
    }
}


// Extension function to load image from URL into ImageView with caching strategy
fun ImageView.loadImageWithCache(url: String, placeHolder: Int, cacheStrategy: DiskCacheStrategy = DiskCacheStrategy.ALL) {
    Glide.with(this.context)
        .load(url) // URL of the image
        .apply(RequestOptions().diskCacheStrategy(cacheStrategy)) // Apply cache strategy
        .error(placeHolder) // Set a default image in case of an error
        .fallback(placeHolder) // Set a placeholder image if the URL is null
        .into(this) // Load into the ImageView
}
