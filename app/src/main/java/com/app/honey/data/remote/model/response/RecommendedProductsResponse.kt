// Main response class
data class RecommendedProductsResponse(
    val message: String?,
    val responseCode: Int?,
    val data: ProductData?
)

data class ProductData(
    val justForYou: Product?,
    val bestPick: Product?
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
