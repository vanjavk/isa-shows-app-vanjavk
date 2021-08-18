package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.networking.Resource
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsRepository
import java.io.File


class ShowsViewModel(
    private val sharedPref: SharedPreferences,
    private val repository: ShowsRepository
) : ViewModel() {

    val userLiveData: LiveData<Resource<User>> = repository.getUserLiveData()

    val topRatedShowsLiveData: LiveData<Resource<List<Show>>> =
        repository.getTopRatedShowsLiveData()

    val showsResultLiveData: LiveData<Resource<Boolean>> = repository.getShowsResultLiveData()

    val changeProfilePictureResultLiveData=repository.getChangeProfilePictureResultLiveData()

    fun getShowsLiveData(): LiveData<List<Show>> = repository.getShowsLiveData()

    fun fetchTopRatedShows() = repository.fetchTopRatedShows()

    fun fetchShows() = repository.fetchShows()

    fun uploadProfilePicture(file: File) = repository.uploadProfilePicture(file)

    fun getCurrentUser() = repository.getCurrentUser()

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