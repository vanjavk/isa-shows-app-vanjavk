package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.USER_ID_KEY
import me.vanjavk.isa_shows_app_vanjavk.database.ShowsDatabase
import me.vanjavk.isa_shows_app_vanjavk.model.*
import me.vanjavk.isa_shows_app_vanjavk.model.network.*
import me.vanjavk.isa_shows_app_vanjavk.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


class ShowDetailsViewModel(
    private val sharedPref: SharedPreferences,
    private val database: ShowsDatabase
) : ViewModel() {

    private val showDetailsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val reviewLiveData: MutableLiveData<Review> by lazy {
        MutableLiveData<Review>()
    }

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }


    fun getShowLiveData(id: String): LiveData<ShowEntity> {
        return database.showDao().getShow(id)
    }

    fun getShow(id: String) {
        ApiModule.retrofit.getShow(id).enqueue(object :
            Callback<ShowResponse> {
            override fun onResponse(
                call: Call<ShowResponse>,
                response: Response<ShowResponse>
            ) {
                val show = response.body()?.show
                if (show != null) {
                    Executors.newSingleThreadExecutor().execute {
                        database.showDao().addShow(show.let {
                            ShowEntity(
                                it.id,
                                it.averageRating,
                                it.description,
                                it.imageUrl,
                                it.numberOfReviews,
                                it.title
                            )
                        })
                    }
                    showDetailsResultLiveData.value = true
                } else {
                    //val showEntity = database.showDao().getShow(id)
                    showDetailsResultLiveData.value = false
                }
                getReviews(id)
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())

                showDetailsResultLiveData.value = false
            }

        })
    }

    fun getReviews(showId: String) {
        ApiModule.retrofit.getReviews(showId).enqueue(object :
            Callback<ReviewsResponse> {
            override fun onResponse(
                call: Call<ReviewsResponse>,
                response: Response<ReviewsResponse>
            ) {
                uploadOfflineReviews(showId)
                val reviews = response.body()?.reviews
                if (reviews != null) {
                    Executors.newSingleThreadExecutor().execute {
                        database.userDao().addUsers(reviews.map {
                            UserEntity(
                                it.user.id,
                                it.user.email,
                                it.user.imageUrl
                            )
                        })
                        database.reviewDao().insertAllReviews(
                            reviews.map {
                                ReviewEntity(
                                    it.id.toLong(),
                                    it.comment,
                                    it.rating,
                                    it.showId,
                                    it.user.id
                                )
                            }
                        )
                    }
                    showDetailsResultLiveData.value = true
                }
                showDetailsResultLiveData.value = false
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                showDetailsResultLiveData.value = false
            }

        })
    }

    fun getShowDetailsResultLiveData(): LiveData<Boolean> {
        return showDetailsResultLiveData
    }

    fun getReviewLiveData(): LiveData<Review> {
        return reviewLiveData
    }

    fun getReviewsLiveData(showId: String): LiveData<List<ReviewWithUser>> {
        return database.reviewDao().getReviewsAndUsers(showId)
    }

    fun addReview(rating: Int, comment: String?, showId: Int) {
        ApiModule.retrofit.addReview(AddReviewRequest(rating, comment, showId)).enqueue(object :
            Callback<AddReviewResponse> {
            override fun onResponse(
                call: Call<AddReviewResponse>,
                response: Response<AddReviewResponse>
            ) {
                val review = response.body()?.review
                if (review != null) {
                    getShow(showId.toString())
                    Executors.newSingleThreadExecutor().execute {
                        database.reviewDao().addReview(
                            ReviewEntity(
                                review.id.toLong(),
                                comment,
                                rating,
                                showId,
                                sharedPref.getString(USER_ID_KEY, "").orEmpty()
                            )
                        )
                    }
                }
            }
            override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                Executors.newSingleThreadExecutor().execute {
                    database.reviewDao().addReview(
                        ReviewEntity(
                            null,
                            comment,
                            rating,
                            showId,
                            sharedPref.getString(USER_ID_KEY, "").orEmpty(),
                            false
                        )
                    )
                }
                Log.d("TAG", t.message.toString())
            }
        })
    }

    private fun uploadOfflineReviews(showId: String) {


        Executors.newSingleThreadExecutor().execute {
            val reviewsToUpload = database.reviewDao().getOfflineReviews(
                showId
            )
            reviewsToUpload.forEach {
                it.id?.let { id -> database.reviewDao().removeReview(id) }
                addReview(it.rating, it.comment, it.showId)
            }
        }
    }
}