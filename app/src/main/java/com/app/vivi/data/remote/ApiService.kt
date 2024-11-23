package com.app.vivi.data.remote

import RecommendedProductsResponse
import com.app.vivi.data.remote.model.request.LoginRequest
import com.app.vivi.data.remote.model.response.FindYourNewFavoriteProductResponse
import com.app.vivi.data.remote.model.response.UserReviewsResponse
import com.app.vivi.data.remote.model.response.LoginResponse
import com.app.vivi.data.remote.model.response.PreferenceProductResponse
import com.app.vivi.data.remote.model.response.configuration.AppConfigResponse
import com.app.vivi.data.remote.model.response.searchfragment.ShopByTypeResponse
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

    @GET(ApiNames.PREFERENCE_PRODUCT_DETAIL)
    suspend fun getPreferenceProductDetail(
    ): Response<PreferenceProductResponse>

    @GET(ApiNames.FIND_YOUR_NEW_FAVORITE_PRODUCT)
    suspend fun getFindYourNewFavoriteProduct(
    ): Response<FindYourNewFavoriteProductResponse>

    @GET(ApiNames.USER_REVIEWS)
    suspend fun getUserReviews(
    ): Response<UserReviewsResponse>

    @GET(ApiNames.SHOP_BY_COFFEE_TYPES_API)
    suspend fun getShopByCoffeeTypes(
    ): Response<ShopByTypeResponse>
}