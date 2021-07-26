package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import Show
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.model.RatingInfo
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.model.network.*
import me.vanjavk.isa_shows_app_vanjavk.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowDetailsViewModel(var sharedPref: SharedPreferences) : ViewModel() {

    private val showDetailsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getShowDetailsResultLiveData(): LiveData<Boolean> {
        return showDetailsResultLiveData
    }

    private val showLiveData: MutableLiveData<Show> by lazy {
        MutableLiveData<Show>()
    }

    private val reviewLiveData: MutableLiveData<Review> by lazy {
        MutableLiveData<Review>()
    }

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    private val ratingInfoLiveData: MutableLiveData<RatingInfo> by lazy {
        MutableLiveData<RatingInfo>()
    }

    fun getShow(id: String) {
        ApiModule.retrofit.getShow(id).enqueue(object :
            Callback<ShowResponse> {
            override fun onResponse(
                call: Call<ShowResponse>,
                response: Response<ShowResponse>
            ) {
                showLiveData.value = response.body()?.show
                getReviews(id)
                showDetailsResultLiveData.value = true
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                showDetailsResultLiveData.value = true
            }

        })
    }

    fun getReviews(id: String) {
        ApiModule.retrofit.getReviews(id).enqueue(object :
            Callback<ReviewsResponse> {
            override fun onResponse(
                call: Call<ReviewsResponse>,
                response: Response<ReviewsResponse>
            ) {
                reviewsLiveData.value = response.body()?.reviews
                showDetailsResultLiveData.value = true
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                showDetailsResultLiveData.value = true
            }

        })
    }

    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
    }

    fun getReviewLiveData(): LiveData<Review> {
        return reviewLiveData
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun getRatingInfoLiveData(): LiveData<RatingInfo> {
        return ratingInfoLiveData
    }

    private fun calculateReviews() {
        val numberOfReviews = 0//show.reviews.count()
        val averageRating = 0f//show.reviews.sumOf { it.rating } / numberOfReviews.toFloat()
        ratingInfoLiveData.value = RatingInfo(numberOfReviews, averageRating)
    }

    fun addReview( rating: Int, comment: String?, show_id: Int) {
        ApiModule.retrofit.addReview(AddReviewRequest(rating, comment, show_id)).enqueue(object :
            Callback<AddReviewResponse> {
            override fun onResponse(
                call: Call<AddReviewResponse>,
                response: Response<AddReviewResponse>
            ) {
                reviewLiveData.value = response.body()?.review
                showDetailsResultLiveData.value = true
            }

            override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                showDetailsResultLiveData.value = true
            }

        })

    }


}