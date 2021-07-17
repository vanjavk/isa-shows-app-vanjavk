package me.vanjavk.isa_shows_app_vanjavk.model

import androidx.annotation.DrawableRes

data class Show(
    val ID: String,
    val name: String,
    val description: String,
    val genre: List<Genre>,
    @DrawableRes val imageResourceId: Int
)