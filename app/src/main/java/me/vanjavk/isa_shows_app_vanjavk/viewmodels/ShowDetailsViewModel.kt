package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.models.*
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowDetailsRepository


class ShowDetailsViewModel(
    private val sharedPref: SharedPreferences,
    private val repository: ShowDetailsRepository
) : ViewModel() {

    fun getShowLiveData(showId: String): LiveData<List<Show>> = repository.getShowLiveData(showId)

    fun getReviewsLiveData(showId: String): LiveData<List<Review>> = repository.getReviewsLiveData(showId)

    fun fetchShow(showId: String) {
        repository.getShow(showId)
    }

    fun getReviews(showId: String) {
        repository.getReviews(showId)
    }

    fun addReview(rating: Int, comment: String?, showId: Int) {
        repository.addReview(rating, comment, showId)
    }
}