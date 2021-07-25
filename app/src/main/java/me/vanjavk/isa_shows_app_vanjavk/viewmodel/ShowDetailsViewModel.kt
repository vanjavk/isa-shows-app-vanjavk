package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import Show
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.model.RatingInfo
import me.vanjavk.isa_shows_app_vanjavk.model.Review



class ShowDetailsViewModel : ViewModel() {

    private lateinit var show: Show

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    private val reviewLiveData: MutableLiveData<Review> by lazy {
        MutableLiveData<Review>()
    }

    private val ratingInfoLiveData: MutableLiveData<RatingInfo> by lazy {
        MutableLiveData<RatingInfo>()
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
    }

    fun getReviewLiveData(): LiveData<Review> {
        return reviewLiveData
    }

    fun getRatingInfoLiveData(): LiveData<RatingInfo> {
        return ratingInfoLiveData
    }

    private fun calculateReviews(){
        val numberOfReviews = 0//show.reviews.count()
        val averageRating = 0f//show.reviews.sumOf { it.rating } / numberOfReviews.toFloat()
        ratingInfoLiveData.value = RatingInfo(numberOfReviews,averageRating)
    }

    fun initShow(id: String) {
//        shows.find { it.id == id }.let{
//            if (it==null){
//                show = shows[0]
//            }else{
//                show = it
//                calculateReviews()
//                showLiveData.value = show
//            }
//        }
    }

    fun addReview(name: String, comment: String, rating: Int) {
        val review = Review(name, comment, rating)
//        show.reviews.add(review)
        reviewLiveData.value = review
        calculateReviews()
    }


}