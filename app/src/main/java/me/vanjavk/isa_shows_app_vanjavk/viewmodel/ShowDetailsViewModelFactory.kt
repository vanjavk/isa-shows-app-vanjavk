package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowDetailsViewModelFactory (private val sharedPref: SharedPreferences) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)) {
            return ShowDetailsViewModel(sharedPref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}