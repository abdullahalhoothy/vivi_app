// Main response class
data class RecommendedProductsResponse(
    val justForYou: Product?,
    val bestPick: Product?,
    val bannerImageUrl: String?
)

data class Product(
    val id: String?,
    val productname: String?,
    val productdescription: String?,
    val tagline: String?,
    val producturl: String?,
    val averagerating: String?,
    val totalratings: String?,
    val discountedprice: String?,
    val discountpercentage: String?,
    val originalprice: String?,
    val city: String?,
    val country: String?,
    val countryflagurl: String?,
    val userrating: UserRating?
)

data class UserRating(
    val rating: String?,
    val review: String?,
    val userName: String?,
    val userProfileImageUrl: String?
)
