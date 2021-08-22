package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.USER_EMAIL_KEY
import me.vanjavk.isa_shows_app_vanjavk.USER_ID_KEY
import me.vanjavk.isa_shows_app_vanjavk.USER_IMAGE_URL_KEY
import me.vanjavk.isa_shows_app_vanjavk.models.*
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowDetailsRepository


class ShowDetailsViewModel(
    private val sharedPref: SharedPreferences,
    private val repository: ShowDetailsRepository
) : ViewModel() {

    fun getShowLiveData(showId: String): LiveData<List<Show>> = repository.getShowLiveData(showId)

    fun getReviewsLiveData(showId: String): LiveData<List<Review>> =
        repository.getReviewsLiveData(showId)

    fun fetchShow(showId: String) {
        repository.getShow(showId)
    }

    fun fetchReviews(showId: String) {
        repository.fetchReviews(showId)
    }

    fun addReview(rating: Int, comment: String?, showId: Int): Review {
        repository.addReview(rating, comment, showId)
        return Review(
            "",
            comment = comment,
            rating = rating,
            showId = showId,
            user = User(
                userId = sharedPref.getString(USER_ID_KEY, "").orEmpty(),
                email = sharedPref.getString(USER_EMAIL_KEY, "").orEmpty(),
                imageUrl = sharedPref.getString(USER_IMAGE_URL_KEY, "")
                    .orEmpty()
            )
        )
    }
}