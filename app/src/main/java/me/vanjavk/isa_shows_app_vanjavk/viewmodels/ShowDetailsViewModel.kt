package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import Show
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.USER_ID_KEY
import me.vanjavk.isa_shows_app_vanjavk.models.*
import me.vanjavk.isa_shows_app_vanjavk.models.network.*
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import me.vanjavk.isa_shows_app_vanjavk.repository.repository.ShowDetailsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


class ShowDetailsViewModel(
    private val sharedPref: SharedPreferences,
    private val repository: ShowDetailsRepository
) : ViewModel() {

    private val showDetailsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val showLiveData: MutableLiveData<Show> by lazy { MutableLiveData<Show>() }

    private val reviewLiveData: MutableLiveData<Review> by lazy {
        MutableLiveData<Review>()
    }

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }


    fun getShowLiveData(): LiveData<Show> {
        return showLiveData
        //return database.showDao().getShow(id)
    }

    fun getShow(showId: String) {
        showDetailsResultLiveData.value = false
        repository.getShow(showId, showLiveData, showDetailsResultLiveData)
    }

    fun getReviews(showId: String) {

    }

    fun getShowDetailsResultLiveData(): LiveData<Boolean> {
        return showDetailsResultLiveData
    }

    fun getReviewLiveData(): LiveData<Review> {
        return reviewLiveData
    }

    fun getReviewsLiveData(showId: String): LiveData<List<Review>> {
        return reviewsLiveData
        //return database.reviewDao().getReviewsAndUsers(showId)
    }

    fun addReview(rating: Int, comment: String?, showId: Int) {
//        ApiModule.retrofit.addReview(AddReviewRequest(rating, comment, showId)).enqueue(object :
//            Callback<AddReviewResponse> {
//            override fun onResponse(
//                call: Call<AddReviewResponse>,
//                response: Response<AddReviewResponse>
//            ) {
//                val review = response.body()?.review
//                if (review != null) {
//                    getShow(showId.toString())
//                    Executors.newSingleThreadExecutor().execute {
//                        database.reviewDao().addReview(
//                            ReviewEntity(
//                                review.id.toLong(),
//                                comment,
//                                rating,
//                                showId,
//                                sharedPref.getString(USER_ID_KEY, "").orEmpty()
//                            )
//                        )
//                    }
//                }
//            }
//            override fun onFailure(call: Call<AddReviewResponse>, t: Throwable) {
//                Executors.newSingleThreadExecutor().execute {
//                    database.reviewDao().addReview(
//                        ReviewEntity(
//                            null,
//                            comment,
//                            rating,
//                            showId,
//                            sharedPref.getString(USER_ID_KEY, "").orEmpty(),
//                            false
//                        )
//                    )
//                }
//                Log.d("TAG", t.message.toString())
//            }
//        })
    }

    private fun uploadOfflineReviews(showId: String) {

//
//        Executors.newSingleThreadExecutor().execute {
//            val reviewsToUpload = database.reviewDao().getOfflineReviews(
//                showId
//            )
//            reviewsToUpload.forEach {
//                it.id?.let { id -> database.reviewDao().removeReview(id) }
//                addReview(it.rating, it.comment, it.showId)
//            }
//        }
    }
}