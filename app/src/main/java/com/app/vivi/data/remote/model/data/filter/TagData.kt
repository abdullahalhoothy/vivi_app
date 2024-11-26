package com.app.vivi.data.remote.model.data.filter

data class TagData(
    val name: String,
    val icon: Int,  // Resource ID for the icon
    var isSelected: Boolean = false  // Track selection state
)
