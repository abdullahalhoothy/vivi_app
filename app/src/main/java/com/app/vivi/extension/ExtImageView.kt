import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView

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
