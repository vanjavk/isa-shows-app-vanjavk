package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.vanjavk.isa_shows_app_vanjavk.repository.repository.Repository
import me.vanjavk.isa_shows_app_vanjavk.repository.repository.ShowsRepository

class ViewModelFactory(
    private val sharedPref: SharedPreferences,
    private val repository: Repository?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            modelClass.getConstructor(
                SharedPreferences::class.java
            ).newInstance(sharedPref)
        }else{
            modelClass.getConstructor(
                SharedPreferences::class.java,
                ShowsRepository::class.java
            ).newInstance(sharedPref, repository)
        }
    }

}