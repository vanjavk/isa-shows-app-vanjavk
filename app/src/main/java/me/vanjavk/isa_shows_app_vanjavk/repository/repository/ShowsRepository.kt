package me.vanjavk.isa_shows_app_vanjavk.repository.repository

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import me.vanjavk.isa_shows_app_vanjavk.ShowsApp
import me.vanjavk.isa_shows_app_vanjavk.USER_ID_KEY
import me.vanjavk.isa_shows_app_vanjavk.USER_IMAGE_URL_KEY
import me.vanjavk.isa_shows_app_vanjavk.isOnline
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.models.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.models.network.ShowsResponse
import me.vanjavk.isa_shows_app_vanjavk.models.network.UserResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.Executors


class ShowsRepository(activity: Activity) : Repository(activity) {

    fun getShowsLiveData(): LiveData<List<Show>> = Transformations.map(database.showDao().getAllShows()) {
        it.map {
            it.toShow()
        }
    }

    fun getShows(
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
                        showsResult.value = true
                        Executors.newSingleThreadExecutor().execute {
                            database.showDao().insertAllShows(
                                shows.map {
                                    ShowEntity.from(it)
                                }
                            )

                        }
                    }
                }
                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    Log.d("GETSHOWSFAILURE", t.message.toString())
                    showsResult.value = false
                }
            })
        } else {
            showsResult.value = false
        }
    }

    fun uploadProfilePicture(
        file: File,
        userLiveData: MutableLiveData<User>,
        changeProfilePictureResult: MutableLiveData<Boolean>
    ) {
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        val email: RequestBody = sharedPref.getString("USER_AUTH_UID_TYPE_KEY", "").orEmpty()
            .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        ApiModule.retrofit.changeProfilePicture(email, body).enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                val user = response.body()?.user
                if (user != null) {
                    userLiveData.value = user
                    sharedPref.edit {
                        putString(USER_IMAGE_URL_KEY, user.imageUrl)
                        putString(USER_ID_KEY, user.userId)
                        apply()
                    }
                    changeProfilePictureResult.value = true
                } else {
                    changeProfilePictureResult.value = false
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("CHANGEPROFILEFAILURE", t.message.toString())
                changeProfilePictureResult.value = false
            }
        })
    }

    fun getCurrentUser(
        userLiveData: MutableLiveData<User>, currentUserResult: MutableLiveData<Boolean>
    ) {
        ApiModule.retrofit.getCurrentUser().enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                val user = response.body()?.user
                if (user != null) {
                    with(sharedPref.edit()) {
                        putString(USER_ID_KEY, user.userId)
                        apply()
                    }
                    userLiveData.value = user
                    currentUserResult.value = true
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("GETUSERFAILURE", t.message.toString())
                currentUserResult.value = false
            }
        })
    }
}