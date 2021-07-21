package me.vanjavk.isa_shows_app_vanjavk.model

import androidx.annotation.DrawableRes

data class Show(
    val ID: String,
    var title: String,
    val description: String,
    val genre: List<Genre>,
    @DrawableRes val imageResourceId: Int,
    val reviews: MutableList<Review> = mutableListOf()
) {
    fun genresToString(): String {
        return genre.joinToString(", ") { it.string }
    }
}