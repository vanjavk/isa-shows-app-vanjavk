package me.vanjavk.isa_shows_app_vanjavk.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Pagination(
    @SerialName("count") val count: Int,
    @SerialName("page") val page: Int,
    @SerialName("items") val items: Int,
    @SerialName("pages") val pages: Int
)
