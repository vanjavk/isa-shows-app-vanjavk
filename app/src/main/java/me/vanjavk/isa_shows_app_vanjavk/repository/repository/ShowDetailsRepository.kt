package me.vanjavk.isa_shows_app_vanjavk.repository.repository

import Show
import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import me.vanjavk.isa_shows_app_vanjavk.isOnline
import me.vanjavk.isa_shows_app_vanjavk.models.*
import me.vanjavk.isa_shows_app_vanjavk.models.network.ReviewsResponse
import me.vanjavk.isa_shows_app_vanjavk.models.network.ShowResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class ShowDetailsRepository(activity: Activity) : Repository(activity) {
    fun getShow(
        showId: String,
        showLiveData: MutableLiveData<Show>,
        showResult: MutableLiveData<Boolean>
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
                        showResult.value = true
                        showLiveData.value = show
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
                    } else {
                        showLiveData.value =
                            getShowOffline(showId)// = database.showDao().getShow(id)
                        showResult.value = false
                    }
                }

                override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                    showResult.value = false
                    showLiveData.value = getShowOffline(showId)
                }

            })
        } else {
            showResult.value = false
            showLiveData.value = getShowOffline(showId)
        }
    }

    private fun getShowOffline(showId: String): Show? {
        return database.showDao().getShow(showId).value?.let {
            Show(
                it.id,
                it.averageRating,
                it.description,
                it.imageUrl,
                it.numberOfReviews,
                it.title
            )
        }
    }

    fun getReviews(
        showId: String,
        reviewsLiveData: MutableLiveData<List<Review>>,
        showReviewsResult: MutableLiveData<Boolean>
    ) {
        if (activity.isOnline()) {
            ApiModule.retrofit.getReviews(showId).enqueue(object :
                Callback<ReviewsResponse> {
                override fun onResponse(
                    call: Call<ReviewsResponse>,
                    response: Response<ReviewsResponse>
                ) {
                    //uploadOfflineReviews(showId)
                    val reviews = response.body()?.reviews
                    if (reviews != null) {
                        showReviewsResult.value = true
                        reviewsLiveData.value = reviews
                        Executors.newSingleThreadExecutor().execute {
                            database.reviewDao().insertReviews(
                                reviews.map {
                                    ReviewEntity(
                                        id = it.id.toLong(),
                                        comment = it.comment,
                                        rating = it.rating,
                                        showId = it.showId,
                                        User(
                                            userId = it.user.userId,
                                            email = it.user.email,
                                            imageUrl = it.user.imageUrl
                                        )
                                    )
                                }
                            )
                        }
                    }
                    showReviewsResult.value = false
                }

                override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                    Log.d("TAG", t.message.toString())
                    showReviewsResult.value = false
                }

            })
        } else {
            showReviewsResult.value = false
//            showLiveData.value = getShowOffline(showId)
        }
    }

}