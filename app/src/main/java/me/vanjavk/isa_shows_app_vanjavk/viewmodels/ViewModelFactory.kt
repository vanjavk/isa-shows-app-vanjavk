package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.vanjavk.isa_shows_app_vanjavk.repository.*

class ViewModelFactory(
    private val sharedPref: SharedPreferences?,
    private val repository: Repository?
) : ViewModelProvider.Factory {
    constructor(repository: Repository) : this(null, repository)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            modelClass.getConstructor(
                SharedPreferences::class.java,
                LoginRepository::class.java
            ).newInstance(sharedPref,repository)
        }else if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            modelClass.getConstructor(
                RegistrationRepository::class.java
            ).newInstance(repository)
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