package me.vanjavk.isa_shows_app_vanjavk

import android.util.Patterns

fun CharSequence?.isValidEmail() = !(!isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches())
