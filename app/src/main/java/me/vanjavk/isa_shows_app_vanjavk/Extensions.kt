package me.vanjavk.isa_shows_app_vanjavk

import android.util.Patterns

const val MIN_PASSWORD_LENGTH = 6
const val EXTRA_ID = "EXTRA_ID"
const val EXTRA_EMAIL = "EXTRA_EMAIL"

fun CharSequence?.isValidEmail() = !(!isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches())

