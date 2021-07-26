package me.vanjavk.isa_shows_app_vanjavk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")val id: String,
    @SerialName("email")var email: String,
    @SerialName("image_url")var imageUrl: String?
)
