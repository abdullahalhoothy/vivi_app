package com.app.honey.data.remote.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UpdateFoodAndDrugAllergiesRequest(
    @SerializedName("patientId") val patientId: Int,
    @SerializedName("foodAllergies") val foodAllergies: List<FoodAllergy>,
    @SerializedName("drugAllergies") val drugAllergies: List<String>
) {
    @Parcelize
    data class FoodAllergy(
        @SerializedName("id") val id: Int,
        @SerializedName("isOtherFoodAllergy") val isOtherFoodAllergy: String? = "false",
        @SerializedName("name") val name: String,
        @SerializedName("otherFoodAllergy") val otherFoodAllergy: String? = ""
    ) : Parcelable {
    }
}
