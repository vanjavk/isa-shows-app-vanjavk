package me.vanjavk.isa_shows_app_vanjavk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Review(
    @SerialName("id") val id: String,
    @SerialName("comment") var comment: String?,
    @SerialName("rating") var rating: Int,
    @SerialName("show_id") val showId: Int,
    @SerialName("user") var user: User,
    var sync: Boolean = true,
)
