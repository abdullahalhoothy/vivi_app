package com.app.vivi.data.remote.repo

import com.app.vivi.data.remote.ApiService
import com.app.vivi.data.remote.Resource
import com.app.vivi.extension.fromJsonSafe
import com.app.vivi.extension.toJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Response

abstract class BaseRepo(val apiService: ApiService) {

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>
    ): Resource<T> {
        val response: Response<T>
        return try {
            response = call.invoke()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error","Error in network Request", response.code())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Resource.Error(title = "Error", message = "Error in network Request")
        }

    }

    suspend inline fun <reified Res : Any, Req : Any> safeApiCall(
        email: String,
        request: Req,
        apiUrl: String
    ): Resource<Res> {
        val response: Response<JsonObject>
        return try {
            val jsonRequest = try {
                JsonParser.parseString(request.toJson()).asJsonObject
            } catch (ex: Exception) {
                JsonObject()
            }
            response = apiService.postRequest(apiUrl, email, jsonRequest)
            if (response.isSuccessful && response.body() != null) {
                val jsonObject = response.body()!!.toString()
                val obj = Gson().fromJsonSafe(jsonObject, Res::class.java)

                if (obj != null) {
                    Resource.Success(obj)
                } else {
                    Resource.Error("Error in network Request")
                }
            } else {
                Resource.Error("Error in network Request")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Resource.Error("Error in network Request")
        }

    }
}