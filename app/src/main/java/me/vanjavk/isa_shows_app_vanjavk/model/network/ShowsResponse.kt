package me.vanjavk.isa_shows_app_vanjavk.model.network


import Show
import androidx.annotation.DrawableRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Review

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Show>,
    @SerialName("meta") val meta: Meta
)

@Serializable
data class Meta(
    @SerialName("pagination") val pagination: Pagination,
)

@Serializable
data class Pagination(
    @SerialName("count") val count: Int,
    @SerialName("page") val page: Int,
    @SerialName("items") val items: Int,
    @SerialName("pages") val pages: Int
)
