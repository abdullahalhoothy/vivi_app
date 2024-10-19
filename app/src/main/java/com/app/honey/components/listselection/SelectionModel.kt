package com.app.honey.components.listselection

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectionModel(
    val id: String,
    val text: String,
    val isSelected: Boolean = false
) : Parcelable
