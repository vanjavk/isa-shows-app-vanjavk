package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.vanjavk.isa_shows_app_vanjavk.repository.Repository
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowDetailsRepository
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsRepository

class ViewModelFactory(
    private val sharedPref: SharedPreferences,
    private val repository: Repository?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            modelClass.getConstructor(
                SharedPreferences::class.java
            ).newInstance(sharedPref)
        }else if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            modelClass.getConstructor(
                SharedPreferences::class.java,
                ShowDetailsRepository::class.java
            ).newInstance(sharedPref, repository)
        }else{
            modelClass.getConstructor(
                SharedPreferences::class.java,
                ShowsRepository::class.java
            ).newInstance(sharedPref, repository)
        }
    }

}