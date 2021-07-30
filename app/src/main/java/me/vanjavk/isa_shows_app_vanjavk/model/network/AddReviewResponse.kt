package me.vanjavk.isa_shows_app_vanjavk.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.vanjavk.isa_shows_app_vanjavk.model.Review

@Serializable
data class AddReviewResponse(
    @SerialName("review") val review: Review,
)