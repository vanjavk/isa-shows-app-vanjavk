import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Show(
    @SerialName("id")val id: String,
    @SerialName("average_rating")var averageRating: Float?,
    @SerialName("description")var description: String?,
    @SerialName("image_url")var imageUrl: String,
    @SerialName("no_of_reviews")var numberOfReviews: Int,
    @SerialName("title")var title: String
)