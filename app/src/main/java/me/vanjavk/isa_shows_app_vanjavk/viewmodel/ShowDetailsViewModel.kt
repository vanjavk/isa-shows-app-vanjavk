package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.model.Show


class ShowDetailsViewModel : ViewModel() {

    private lateinit var show: Show

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
    }

    fun initShow(show: Show?) {
        if (show != null) {
            this.show = show
        }
        showLiveData.value = show
    }

    fun addReview(review: Review) {
        show.reviews.add(review)
        showLiveData.value = show
    }


}