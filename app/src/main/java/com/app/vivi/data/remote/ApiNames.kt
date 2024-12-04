package com.app.vivi.data.remote

object ApiNames {
//    const val BASE_URL = "https://testing.com:8443/online/"
    const val BASE_URL = "http://37.27.201.200:8000/"
    const val LOGIN = "${BASE_URL}login"

    //api
    const val CONFIGURATION = "${BASE_URL}configuration"
    const val RECOMMENDED_PRODUCTS = "${BASE_URL}recommended_products"
    const val PREFERENCE_PRODUCT_DETAIL = "${BASE_URL}preference_product_detail"
    const val FIND_YOUR_NEW_FAVORITE_PRODUCT = "${BASE_URL}find_your_new_favorite_product"
    const val USER_REVIEWS = "${BASE_URL}user_reviews"

    const val SHOP_BY_COFFEE_TYPES_API = "${BASE_URL}coffee-types"
    const val SHOP_BY_COFFEE_BEAN_TYPES_API = "${BASE_URL}coffee-bean-types"
    const val SHOP_BY_COUNTRIES_API = "${BASE_URL}countries"
    const val SHOP_BY_REGIONS_API = "${BASE_URL}regions"

    const val FILTERED_PRODUCTS_API = "${BASE_URL}filtered-products"
    const val PRODUCT_FILTERS_API = "${BASE_URL}product-filters"
}

object HeadersNames {
    const val EMAIL = "email"
    const val LANGUAGE = "language"
    const val CHANNEL = "channel"
}

object HeaderValues {
    const val LANGUAGE = "english"
    const val CHANNEL = "android"
}