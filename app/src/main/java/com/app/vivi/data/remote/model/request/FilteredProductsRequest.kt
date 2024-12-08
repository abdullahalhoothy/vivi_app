package com.app.vivi.data.remote.model.request

data class FilteredProductsRequest(
    var typeIds: List<Int> = emptyList(),
    val minRatingValue: String? = "",
    val minPrice: String? = "",
    val maxPrice: String? = "",
    var countryNames: List<String> = emptyList(),
    var regionIds: List<Int> = emptyList(),
    var rawMaterialIds: List<Int> = emptyList(),
    var styleIds: List<Int> = emptyList(),
    var sizeIds: List<Int> = emptyList()
)
