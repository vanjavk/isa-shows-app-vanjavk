package me.vanjavk.isa_shows_app_vanjavk.models.network

import Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowResponse(
    @SerialName("show")val show: Show
)