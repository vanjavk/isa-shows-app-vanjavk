package me.vanjavk.isa_shows_app_vanjavk

import android.util.Patterns
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Show

const val MIN_PASSWORD_LENGTH = 6

fun CharSequence?.isValidEmail() = !(!isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches())

fun CharSequence.getUsername() = this.substring(0, this.indexOf('@')).orEmpty()


