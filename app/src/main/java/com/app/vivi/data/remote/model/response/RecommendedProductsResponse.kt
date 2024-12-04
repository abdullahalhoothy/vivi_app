// Main response class
data class RecommendedProductsResponse(
    val justForYou: RecommendedProductData?,
    val bestPick: RecommendedProductData?,
    val bannerImageUrl: String?
)

data class RecommendedProductData(
    val id: String?,
    val name: String?,
    val description: String?,
    val tagline: String?,
    val producturl: String?,
    val imageurl: String?,
    val averagerating: String?,
    val totalratings: String?,
    val discountedprice: String?,
    val discountpercentage: String?,
    val originalprice: String?,
    val city: String?,
    val country: String?,
    val countryflagurl: String?,
    val userrating: RecommendedProductsUserRating?
)

data class RecommendedProductsUserRating(
    val rating: String?,
    val review: String?,
    val userName: String?,
    val description: String?,
    val userProfileImageUrl: String?
)
