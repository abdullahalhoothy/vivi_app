package com.app.honey.features.homescreen.display.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PatientModel(
    val name: String
): Parcelable
