package com.app.vivi.data.remote

object ApiNames {
//    const val BASE_URL = "https://testing.com:8443/online/"
    const val BASE_URL = "http://37.27.201.200:8000/"
    const val LOGIN = "${BASE_URL}app/login"

    //api
    const val CONFIGURATION = "${BASE_URL}configuration"
    const val RECOMMENDED_PRODUCTS = "${BASE_URL}recommended_products"
    const val PREFERENCE_PRODUCT_DETAIL = "${BASE_URL}preference_product_detail"
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