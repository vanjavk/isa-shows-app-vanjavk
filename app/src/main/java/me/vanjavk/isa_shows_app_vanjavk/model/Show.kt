import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//package me.vanjavk.isa_shows_app_vanjavk.model
//
//import androidx.annotation.DrawableRes
//
@Serializable
data class Show(
    @SerialName("id")val id: String,
    @SerialName("average_rating")var averageRating: String?,
    @SerialName("description")var description: String?,
    @SerialName("image_url")var imageUrl: String,
    @SerialName("no_of_reviews")var numberOfReviews: Int,
    @SerialName("title")var title: String
)