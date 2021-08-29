package me.vanjavk.isa_shows_app_vanjavk.models.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Meta(
    @SerialName("pagination") val pagination: Pagination,
)