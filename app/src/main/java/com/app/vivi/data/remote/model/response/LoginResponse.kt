package com.app.vivi.data.remote.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("localId")
    val localId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("idToken")
    val idToken: String,
    @SerializedName("registered")
    val registered: Boolean,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("expiresIn")
    val expiresIn: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("firebase")
    val firebase: Firebase
) : Parcelable {

    @Parcelize
    data class Firebase(
        @SerializedName("identities")
        val identities: Identities,
        @SerializedName("sign_in_provider")
        val signInProvider: String
    ) : Parcelable

    @Parcelize
    data class Identities(
        @SerializedName("email")
        val email: List<String>
    ) : Parcelable
}