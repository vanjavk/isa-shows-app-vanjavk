package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.vanjavk.isa_shows_app_vanjavk.database.ShowsDatabase

class ViewModelFactory (val sharedPref: SharedPreferences, val database: ShowsDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(SharedPreferences::class.java, ShowsDatabase::class.java).newInstance(sharedPref, database)
    }

}