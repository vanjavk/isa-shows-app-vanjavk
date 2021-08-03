package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.vanjavk.isa_shows_app_vanjavk.repository.Repository
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsDatabase

class ViewModelFactory(
    private val sharedPref: SharedPreferences,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            SharedPreferences::class.java,
            Repository::class.java
        ).newInstance(sharedPref, repository)
    }

}