package me.vanjavk.isa_shows_app_vanjavk.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.vanjavk.isa_shows_app_vanjavk.model.User

@Serializable
data class UserResponse(
    @SerialName("user") val user: User
)
