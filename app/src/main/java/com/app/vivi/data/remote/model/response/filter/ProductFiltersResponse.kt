package com.app.vivi.data.remote.model.response.filter

data class ProductFiltersResponse(
    val coffeeTypes: List<CoffeeType>?,
    val coffeeBeanTypes: List<CoffeeBeanType>?,
    val countries: List<Country>?,
    val regions: List<Region>?,
    val coffeeStyles: List<CoffeeStyle>?,
    val sizes: List<Size>?
)

data class CoffeeType(
    val id: Int?,
    val name: String?,
    val count: Int?,
    val isSelected: Boolean = false
)

data class CoffeeBeanType(
    val id: Int?,
    val name: String?,
    val count: Int?,
    val isSelected: Boolean = false
)

data class Country(
    val id: Int?,
    val name: String?,
    val count: Int?,
    val isSelected: Boolean = false
)

data class Region(
    val id: Int?,
    val name: String?,
    val count: Int?,
    val isSelected: Boolean = false
)

data class CoffeeStyle(
    val id: Int?,
    val name: String?,
    val count: Int?
)

data class Size(
    val id: Int?,
    val sizeValue: Double?,
    val count: Int?
)