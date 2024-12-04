package com.app.vivi.data.remote.model.request

data class FilteredProductsRequest(
    val typeIds: List<Int> = emptyList(),
    val minRatingValue: String? = null,
    val minPrice: String? = null,
    val maxPrice: String? = null,
    val countryNames: List<String> = emptyList(),
    val regionIds: List<Int> = emptyList(),
    val rawMaterialIds: List<Int> = emptyList(),
    val styleIds: List<Int> = emptyList(),
    val sizeIds: List<Int> = emptyList()
)
