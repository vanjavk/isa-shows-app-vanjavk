package me.vanjavk.isa_shows_app_vanjavk.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.vanjavk.isa_shows_app_vanjavk.model.Review

@Serializable
data class ReviewsResponse(
    @SerialName("reviews") val reviews: List<Review>,
    @SerialName("meta") val meta: Meta
)
