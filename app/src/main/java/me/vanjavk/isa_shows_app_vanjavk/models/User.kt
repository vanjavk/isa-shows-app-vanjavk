package me.vanjavk.isa_shows_app_vanjavk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")val userId: String,
    @SerialName("email")var email: String,
    @SerialName("image_url")var imageUrl: String?
)
