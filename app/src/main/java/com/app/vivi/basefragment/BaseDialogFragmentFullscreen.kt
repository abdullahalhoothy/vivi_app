package com.app.vivi.basefragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.app.vivi.baseviewmodel.BaseViewModel
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.errorDialog
import com.app.vivi.extension.progressDialog
import com.app.vivi.extension.successDialog
import com.app.vivi.extension.visibility
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest

abstract class BaseDialogFragmentFullscreen<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    private var progressDialog: AlertDialog? = null
    private var errorDialog: AlertDialog? = null
    private var successDialog: AlertDialog? = null

    abstract fun getMyViewModel(): BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)

        // Setting up the dialog's background and features
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = progressDialog()
        observeLoader()
    }


    override fun onStart() {
        super.onStart()

        // Make the BottomSheetDialog full-screen
        val dialog = dialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true // Skip collapsed state
        behavior.isFitToContents = true // Makes it fit the content
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    // Function to observe loader state changes
    private fun observeLoader() {
        collectWhenStarted {
            getMyViewModel().loader.collectLatest { state ->
                when (state) {
                    is ErrorLoadingState.Error -> {
                        progressDialog?.visibility(false)
                        errorDialog = errorDialog(state.errorModel) {
                            getMyViewModel().hideError()
                        }
                        errorDialog?.show()
                    }

                    ErrorLoadingState.Idle -> {
                        progressDialog?.visibility(false)
                        errorDialog?.dismiss()
                    }

                    ErrorLoadingState.Loading -> {
                        progressDialog?.visibility(true)
                        errorDialog?.dismiss()
                    }

                    is ErrorLoadingState.Success -> {
                        progressDialog?.visibility(false)
                        successDialog = successDialog(state.errorModel) {
                            getMyViewModel().hideError()
                            successDialog?.dismiss()
                        }
                        successDialog?.show()
                    }
                }
            }
        }
    }
}
