package me.vanjavk.isa_shows_app_vanjavk.models.network


import Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Show>,
    @SerialName("meta") val meta: Meta
)

