package com.app.honey.data.remote

import RecommendedProductsResponse
import com.app.honey.data.remote.model.request.GetMenuRequest
import com.app.honey.data.remote.model.request.GetPatientRequest
import com.app.honey.data.remote.model.request.LoginRequest
import com.app.honey.data.remote.model.response.LoginResponse
import com.app.honey.data.remote.model.response.configuration.ConfigurationResponse
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
    ): Response<ConfigurationResponse>

    @GET(ApiNames.RECOMMENDED_PRODUCTS)
    suspend fun getRecommendedProducts(
    ): Response<RecommendedProductsResponse>
}