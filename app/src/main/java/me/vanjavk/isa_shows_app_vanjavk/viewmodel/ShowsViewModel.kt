package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.shows

class ShowsViewModel : ViewModel() {

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun initShows() {
        showsLiveData.value = shows
    }

}