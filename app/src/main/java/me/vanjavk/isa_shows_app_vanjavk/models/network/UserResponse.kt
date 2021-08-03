package me.vanjavk.isa_shows_app_vanjavk.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.vanjavk.isa_shows_app_vanjavk.models.User

@Serializable
data class UserResponse(
    @SerialName("user") val user: User
)
