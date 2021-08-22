package me.vanjavk.isa_shows_app_vanjavk.repository

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.models.*
import me.vanjavk.isa_shows_app_vanjavk.models.network.AddReviewRequest
import me.vanjavk.isa_shows_app_vanjavk.models.network.AddReviewResponse
import me.vanjavk.isa_shows_app_vanjavk.models.network.ReviewsResponse
import me.vanjavk.isa_shows_app_vanjavk.models.network.ShowResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import me.vanjavk.isa_shows_app_vanjavk.networking.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowDetailsRepository(activity: Activity) : Repository(activity) {

    private val reviewsResultLiveData = MutableLiveData<Resource<Boolean>>()

    private val addReviewResultLiveData = MutableLiveData<Resource<Boolean>>()

    fun getShowLiveData(showId: String): LiveData<List<Show>> =
        Transformations.map(database.showDao().getShow(showId)) {
            it.map { it.toShow() }
        }

    fun getReviewsLiveData(showId: String): LiveData<List<Review>> =
        Transformations.map(database.reviewDao().getReviews(showId)) {
            it.map { it.toReview() }
        }

    fun getShow(
        showId: String
    ) {
        if (activity.isOnline()) {
            ApiModule.retrofit.getShow(showId).enqueue(object :
                Callback<ShowResponse> {
                override fun onResponse(
                    call: Call<ShowResponse>,
                    response: Response<ShowResponse>
                ) {
                    val show = response.body()?.show
                    if (show != null) {
                        Executors.newSingleThreadExecutor().execute {
                            database.showDao().addShow(
                                ShowEntity.from(show)
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                }

            })
        }
    }

    fun fetchReviews(
        showId: String
    ) {
        reviewsResultLiveData.value = Resource.loading(true)
        uploadOfflineReviews()
        if (activity.isOnline()) {
            ApiModule.retrofit.getReviews(showId).enqueue(object :
                Callback<ReviewsResponse> {
                override fun onResponse(
                    call: Call<ReviewsResponse>,
                    response: Response<ReviewsResponse>
                ) {
                    val reviews = response.body()?.reviews
                    if (reviews != null) {
                        reviewsResultLiveData.postValue(Resource.success(true))
                        Executors.newSingleThreadExecutor().execute {
                            database.reviewDao().insertReviews(
                                reviews.map {
                                    ReviewEntity.from(it)
                                }
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                    reviewsResultLiveData.postValue(Resource.error("", false))
                }
            })
        } else {
            reviewsResultLiveData.value = Resource.error(NO_INTERNET_ERROR, false)
        }
    }

    private fun uploadOfflineReviews() {
        if (activity.isOnline()) {
            Executors.newSingleThreadExecutor().execute {
                val reviewsToUpload = database.reviewDao().getOfflineReviews()
                database.reviewDao().removeReviews(reviewsToUpload)
                reviewsToUpload.forEach {
                    addReview(it.rating, it.comment, it.showId, true)
                }
            }
        }
    }

    fun addReview(
        rating: Int, comment: String?, showId: Int, offline: Boolean = false
    ) {
        reviewsResultLiveData.value = Resource.loading(!offline)
        if (activity.isOnline()) {
            ApiModule.retrofit.addReview(AddReviewRequest(rating, comment, showId)).enqueue(object :
                Callback<AddReviewResponse> {
                override fun onResponse(
                    call: Call<AddReviewResponse>,
                    response: Response<AddReviewResponse>
                ) {
                    val review = response.body()?.review
                    if (review != null) {
                        addReviewResultLiveData.postValue(Resource.success(!offline))
                        Executors.newSingleThreadExecutor().execute {
                            database.reviewDao().addReview(
                                ReviewEntity.from(review)
                            )
                        }
                        getShow(showId.toString())
                    }
                }

                override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
                    addReviewResultLiveData.postValue(Resource.error("", !offline))
                    Executors.newSingleThreadExecutor().execute {
                        database.reviewDao().addReview(
                            ReviewEntity(
                                id = null,
                                comment = comment,
                                rating = rating,
                                showId = showId,
                                user = User(
                                    userId = sharedPref.getString(USER_ID_KEY, "").orEmpty(),
                                    email = sharedPref.getString(USER_EMAIL_KEY, "").orEmpty(),
                                    imageUrl = sharedPref.getString(USER_IMAGE_URL_KEY, "")
                                        .orEmpty()
                                ),
                                sync = false
                            )
                        )
                    }
                }
            })
        } else {
            addReviewResultLiveData.value = Resource.error(NO_INTERNET_ERROR, !offline)
            Executors.newSingleThreadExecutor().execute {
                database.reviewDao().addReview(
                    ReviewEntity(
                        id = null,
                        comment = comment,
                        rating = rating,
                        showId = showId,
                        user = User(
                            userId = sharedPref.getString(USER_ID_KEY, "").orEmpty(),
                            email = sharedPref.getString(USER_EMAIL_KEY, "").orEmpty(),
                            imageUrl = sharedPref.getString(USER_IMAGE_URL_KEY, "")
                                .orEmpty()
                        ),
                        sync = false
                    )
                )
            }
        }
    }
}