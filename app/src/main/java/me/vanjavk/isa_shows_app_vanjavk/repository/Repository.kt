package me.vanjavk.isa_shows_app_vanjavk.repository

import Show
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import me.vanjavk.isa_shows_app_vanjavk.ShowsApp
import me.vanjavk.isa_shows_app_vanjavk.isOnline
import me.vanjavk.isa_shows_app_vanjavk.models.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.models.network.ShowsResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


class Repository(private val activity: Activity) {

    private val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    private val database = (activity.application as ShowsApp).showsDatabase

    fun getShows(
        showsLiveData: MutableLiveData<List<Show>>,
        showsResult: MutableLiveData<Boolean>
    ) {
        if (activity.isOnline()) {
            ApiModule.retrofit.getShows().enqueue(object :
                Callback<ShowsResponse> {
                override fun onResponse(
                    call: Call<ShowsResponse>,
                    response: Response<ShowsResponse>
                ) {
                    val shows = response.body()?.shows
                    if (shows != null) {
                        Executors.newSingleThreadExecutor().execute {
                            database.showDao().insertAllShows(
                                shows.map {
                                    ShowEntity(
                                        id = it.id,
                                        averageRating = it.averageRating,
                                        description = it.description,
                                        imageUrl = it.imageUrl,
                                        numberOfReviews = it.numberOfReviews,
                                        title = it.title
                                    )
                                }
                            )

                        }
                        showsLiveData.value = shows
                        showsResult.value = true
                    }
                }
                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    Log.d("GETSHOWSFAILURE", t.message.toString())
                    showsLiveData.value = getShowsOffline()
                }
            })
        } else {
            showsLiveData.value = getShowsOffline()
        }
    }

    fun getShowsOffline(): List<Show>? {
        return database.showDao().getAllShows().value?.map{
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
}
