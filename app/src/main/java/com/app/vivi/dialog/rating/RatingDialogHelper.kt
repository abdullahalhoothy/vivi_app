package com.app.vivi.dialog.rating

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import com.app.vivi.R
import com.app.vivi.databinding.DialogRatingBinding

class RatingDialogHelper(
    private val context: Context,
    private val onSubmit: (rating: Float, review: String) -> Unit
) {

    fun showRatingDialog() {
        val dialog = Dialog(context, R.style.FullScreenDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Inflate and set up the ViewBinding
        val binding = DialogRatingBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        // Set full-screen layout after showing the dialog
        dialog.setOnShowListener {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        binding.closeButton.setOnClickListener { dialog.dismiss() }

        binding.tvDone.setOnClickListener {
            val rating = binding.circularRatingView.getRating()
            val reviewText = binding.reviewEditText.text.toString()
            onSubmit(rating, reviewText)
            dialog.dismiss()
        }

        binding.circularRatingView.setOnTouchListener { _, _ ->
//            binding.circularRatingView.setProgress(binding.circularRatingView.getRating())
            false
        }

        dialog.show()
    }
}
