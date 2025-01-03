package com.app.vivi.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.vivi.R
import com.app.vivi.databinding.DialogErrorBinding
import com.app.vivi.databinding.DialogSuccessBinding
import com.app.vivi.domain.model.ErrorModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

fun Fragment.collectWhenStarted(action: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            action.invoke()
        }
    }
}

fun Fragment.progressDialog(): AlertDialog {
    return MaterialAlertDialogBuilder(requireContext()).apply {
        setView(R.layout.loader)
    }.create().apply {
        setCancelable(false)
        window?.let {
            it.setDimAmount(0F)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}

fun Fragment.errorDialog(errorModel: ErrorModel, onClick: () -> Unit): AlertDialog {
    val binding = DialogErrorBinding.inflate(layoutInflater)
    return MaterialAlertDialogBuilder(requireContext()).apply {
        setView(binding.root)

        binding.apply {
            tvTitle.text = errorModel.title
            tvMessage.text = errorModel.message
            btnOk.setOnClickListener {
                onClick()
            }
        }
    }.create().apply {
        setCancelable(false)
        window?.let {
            it.setDimAmount(0F)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}

fun Fragment.successDialog(errorModel: ErrorModel, onClick: () -> Unit): AlertDialog {
    val binding = DialogSuccessBinding.inflate(layoutInflater)
    return MaterialAlertDialogBuilder(requireContext()).apply {
        setView(binding.root)

        binding.apply {
            tvTitle.text = errorModel.title
            tvMessage.text = errorModel.message
            btnOk.setOnClickListener {
                onClick()
            }
        }
    }.create().apply {
        setCancelable(false)
        window?.let {
            it.setDimAmount(0F)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}


