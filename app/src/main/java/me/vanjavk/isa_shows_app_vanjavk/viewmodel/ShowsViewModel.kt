package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.shows
import java.io.File

class ShowsViewModel : ViewModel() {

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    private val userProfilePictureLiveData: MutableLiveData<File> by lazy {
        MutableLiveData<File>()
    }

    fun getUserProfilePictureLiveData(): LiveData<File> {
        return userProfilePictureLiveData
    }

    fun initShows() {
        showsLiveData.value = shows
    }

    fun setImage(file: File) {
        userProfilePictureLiveData.value = file
    }

}