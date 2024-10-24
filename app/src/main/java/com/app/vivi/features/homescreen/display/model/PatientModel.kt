package com.app.vivi.features.homescreen.display.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PatientModel(
    val name: String
): Parcelable
