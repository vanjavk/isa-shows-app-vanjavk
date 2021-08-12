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

    private val showDetailsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val reviewsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val showLiveData: MutableLiveData<Show> by lazy { MutableLiveData<Show>() }

    private val reviewLiveData: MutableLiveData<Review> by lazy {
        MutableLiveData<Review>()
    }

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    fun getShowLiveData(showId: String): LiveData<List<Show>> = repository.getShowLiveData(showId)

    fun getReviewsLiveData(showId: String): LiveData<List<Review>> = repository.getReviewsLiveData(showId)

    fun getShowDetailsResultLiveData(): LiveData<Boolean> {
        return showDetailsResultLiveData
    }

    fun getReviewLiveData(): LiveData<Review> {
        return reviewLiveData
    }

    fun getShow(showId: String) {
        showDetailsResultLiveData.value = false
        repository.getShow(showId, showDetailsResultLiveData)
    }

    fun getReviews(showId: String) {
        reviewsResultLiveData.value = false
        repository.getReviews(showId, reviewsLiveData, reviewsResultLiveData)
    }

    fun addReview(rating: Int, comment: String?, showId: Int) {
        repository.addReview(rating, comment, showId, reviewsResultLiveData)
    }
}