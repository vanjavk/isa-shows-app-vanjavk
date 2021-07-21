package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.model.RatingInfo
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.shows


class ShowDetailsViewModel : ViewModel() {

    private lateinit var show: Show

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    private val ratingInfoLiveData: MutableLiveData<RatingInfo> by lazy {
        MutableLiveData<RatingInfo>()
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
    }

    fun getRatingInfoLiveData(): LiveData<RatingInfo> {
        return ratingInfoLiveData
    }

    private fun calculateReviews(){
        val numberOfReviews = show.reviews.count()
        val averageRating = show.reviews.sumOf { it.rating } / numberOfReviews.toFloat()
        ratingInfoLiveData.value = RatingInfo(numberOfReviews,averageRating)
    }

    fun initShow(id: String) {
        show = shows.find { it.id == id }!!//help
//        if (show == null) {
//            return
//        }
        calculateReviews()
        showLiveData.value = show
    }

    fun addReview(name: String, comment: String, rating: Int) {
        val review = Review(name, comment, rating)
        show.reviews.add(review)
        calculateReviews()
        showLiveData.value = show
    }


}