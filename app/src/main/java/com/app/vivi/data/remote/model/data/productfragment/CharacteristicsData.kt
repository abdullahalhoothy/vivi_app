package com.app.vivi.data.remote.model.data.productfragment

data class CharacteristicsData(
    val id: Int,
    val labelLight: String,
    val labelBold: String,
    var sliderValue: Float // Stores the current slider value
)
