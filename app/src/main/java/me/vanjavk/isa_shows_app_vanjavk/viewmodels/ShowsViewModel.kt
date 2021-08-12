package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsRepository
import java.io.File


class ShowsViewModel(
    private val sharedPref: SharedPreferences,
    private val repository: ShowsRepository
) : ViewModel() {

    private val showsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val changeProfilePictureResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val userLiveData: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    private val currentUserResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getShowsLiveData(): LiveData<List<Show>> = repository.getShowsLiveData()

    fun getShowsResultLiveData(): LiveData<Boolean> {
        return showsResultLiveData
    }

    fun getChangeProfilePictureResultLiveDataLiveData(): LiveData<Boolean> {
        return changeProfilePictureResultLiveData
    }

    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }

    fun getCurrentUserResultLiveData(): LiveData<Boolean> {
        return currentUserResultLiveData
    }

    fun getShows() {
        showsResultLiveData.value = false
        repository.getShows(showsResultLiveData)
    }

    fun uploadProfilePicture(file: File) {
        showsResultLiveData.value = false
        repository.uploadProfilePicture(file, userLiveData, changeProfilePictureResultLiveData)
    }

    fun getCurrentUser() {
        currentUserResultLiveData.value = false
        repository.getCurrentUser(userLiveData, currentUserResultLiveData)
    }

    fun logout() {
        with(sharedPref.edit()) {
            putBoolean(
                REMEMBER_ME_KEY,
                false
            )
            remove(USER_ID_KEY)
            remove(USER_AUTH_ACCESS_TOKEN_TYPE_KEY)
            remove(USER_AUTH_CLIENT_TYPE_KEY)
            remove(USER_AUTH_UID_TYPE_KEY)
            apply()
        }
    }
}