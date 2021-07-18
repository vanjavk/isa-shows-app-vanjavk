package me.vanjavk.isa_shows_app_vanjavk.model

import androidx.annotation.DrawableRes

data class Show(
    val ID: String,
    val title: String,
    val description: String,
    val genre: List<Genre>,

    @DrawableRes val imageResourceId: Int
) {
    fun genresToString(): String {
        return genre.joinToString(", ")
    }
}