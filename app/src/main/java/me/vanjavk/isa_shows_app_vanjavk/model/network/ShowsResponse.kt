package me.vanjavk.isa_shows_app_vanjavk.model.network


import Show
import androidx.annotation.DrawableRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Review

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Show>
)

