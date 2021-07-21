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

    private lateinit var reviews: MutableList<Review>

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    private val ratingInfoLiveData: MutableLiveData<RatingInfo> by lazy {
        MutableLiveData<RatingInfo>()
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getRatingInfoLiveData(): LiveData<RatingInfo> {
        return ratingInfoLiveData
    }

    private fun calculateReviews(){
        val numberOfReviews = reviews.count()
        val averageRating = reviews.sumOf { it.rating } / numberOfReviews.toFloat()
        ratingInfoLiveData.value = RatingInfo(numberOfReviews,averageRating)
    }

    fun initShow(id: String) {
        shows.find { it.id == id }.let{
            if (it==null){
                //error message
            }else{
                show = it
                reviews = show.reviews
                calculateReviews()
                showLiveData.value = show
            }
        }
    }

    fun addReview(name: String, comment: String, rating: Int) {
        val review = Review(name, comment, rating)
        show.reviews.add(review)
//        reviews.add(review)
        calculateReviews()
//        reviewsLiveData.value = reviews
//        showLiveData.value = show
    }


}