package com.app.vivi.data.remote

import RecommendedProductsResponse
import com.app.vivi.data.remote.model.request.LoginRequest
import com.app.vivi.data.remote.model.response.LoginResponse
import com.app.vivi.data.remote.model.response.configuration.AppConfigResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @POST
    suspend fun postRequest(
        @Url url: String,
        @Header(HeadersNames.EMAIL) email: String,
        @Body body: JsonObject
    ): Response<JsonObject>


    @POST(ApiNames.LOGIN)
    suspend fun login(
        @Header(HeadersNames.EMAIL) email: String,
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @GET(ApiNames.CONFIGURATION)
    suspend fun configuration(
    ): Response<AppConfigResponse>

    @GET(ApiNames.RECOMMENDED_PRODUCTS)
    suspend fun getRecommendedProducts(
    ): Response<RecommendedProductsResponse>
}