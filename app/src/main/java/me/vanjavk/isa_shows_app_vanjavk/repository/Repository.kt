package me.vanjavk.isa_shows_app_vanjavk.repository

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import me.vanjavk.isa_shows_app_vanjavk.ShowsApp


abstract class Repository(val activity: Activity) {

    val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
    val database = (activity.application as ShowsApp).showsDatabase
}
