package me.vanjavk.isa_shows_app_vanjavk

import android.app.Application
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsDatabase


class ShowsApp : Application() {

    val showsDatabase by lazy {
        ShowsDatabase.getDatabase(this)
    }

}