package com.app.vivi.basefragment

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.app.vivi.R

abstract class FullScreenDialogFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) :
    BaseDialogFragmentVM<VB>(inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.MyStyle)
    }
}