package com.app.vivi.basefragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.app.vivi.baseviewmodel.BaseViewModel
import com.app.vivi.extension.collectWhenStarted
import com.app.vivi.extension.errorDialog
import com.app.vivi.extension.progressDialog
import com.app.vivi.extension.successDialog
import com.app.vivi.extension.visibility
import kotlinx.coroutines.flow.collectLatest

abstract class BaseDialogFragmentVM<VB : ViewBinding>(inflate: Inflate<VB>) :
    BaseDialogFragmentVB<VB>(inflate) {

    private lateinit var progressDialog: AlertDialog
    private var errorDialog: AlertDialog? = null
    private var successDialog: AlertDialog? = null
    private lateinit var prevState: ErrorLoadingState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = progressDialog()
        observeLoader()
    }

    private fun observeLoader() {
        collectWhenStarted {
            getMyViewModel().loader.collectLatest { state ->
                when (state) {
                    is ErrorLoadingState.Error -> {
                        progressDialog.visibility(false)
                        errorDialog = errorDialog(state.errorModel) {
                            getMyViewModel().hideError()
                        }
                        errorDialog?.show()
                    }
                    ErrorLoadingState.Idle -> {
                        progressDialog.visibility(false)
                        errorDialog?.dismiss()
                    }
                    ErrorLoadingState.Loading -> {
                        progressDialog.visibility(true)
                        errorDialog?.dismiss()
                    }
                    is ErrorLoadingState.Success -> {
                        progressDialog.visibility(false)
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

    abstract fun getMyViewModel(): BaseViewModel


}