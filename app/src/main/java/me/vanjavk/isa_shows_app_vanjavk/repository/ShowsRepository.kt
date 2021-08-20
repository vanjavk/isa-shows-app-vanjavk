package me.vanjavk.isa_shows_app_vanjavk.repository

import android.app.Activity
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import me.vanjavk.isa_shows_app_vanjavk.NO_INTERNET_ERROR
import me.vanjavk.isa_shows_app_vanjavk.USER_ID_KEY
import me.vanjavk.isa_shows_app_vanjavk.USER_IMAGE_URL_KEY
import me.vanjavk.isa_shows_app_vanjavk.isOnline
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.models.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.models.network.ShowsResponse
import me.vanjavk.isa_shows_app_vanjavk.models.network.UserResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import me.vanjavk.isa_shows_app_vanjavk.networking.Resource
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

    private val userLiveData = MutableLiveData<Resource<User>>()

    private val changeProfilePictureResultLiveData = MutableLiveData<Resource<Boolean>>()

    private val showsResultLiveData = MutableLiveData<Resource<Boolean>>()

    private val topRatedShowsLiveData = MutableLiveData<Resource<List<Show>>>()

    fun getUserLiveData(): LiveData<Resource<User>> = userLiveData

    fun getChangeProfilePictureResultLiveData(): LiveData<Resource<Boolean>> =
        changeProfilePictureResultLiveData

    fun getTopRatedShowsLiveData(): LiveData<Resource<List<Show>>> = topRatedShowsLiveData

    fun getShowsResultLiveData(): LiveData<Resource<Boolean>> = showsResultLiveData

    fun getShowsLiveData(): LiveData<List<Show>> =
        Transformations.map(database.showDao().getAllShows()) {
            it.map {
                it.toShow()
            }
        }

    fun fetchTopRatedShows() {
        topRatedShowsLiveData.value = Resource.loading(topRatedShowsLiveData.value?.data)
        if (activity.isOnline()) {
            ApiModule.retrofit.getTopRatedShows().enqueue(object :
                Callback<ShowsResponse> {
                override fun onResponse(
                    call: Call<ShowsResponse>,
                    response: Response<ShowsResponse>
                ) {
                    response.body()
                        ?.let { topRatedShowsLiveData.postValue(Resource.success(it.shows)) }
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    topRatedShowsLiveData.postValue(Resource.error(null, null))
                }
            })
        } else {
            topRatedShowsLiveData.postValue(Resource.error(NO_INTERNET_ERROR, null))
        }
    }

    fun fetchShows() {
        showsResultLiveData.value = Resource.loading(true)
        if (activity.isOnline()) {
            ApiModule.retrofit.getShows().enqueue(object :
                Callback<ShowsResponse> {
                override fun onResponse(
                    call: Call<ShowsResponse>,
                    response: Response<ShowsResponse>
                ) {
                    val shows = response.body()?.shows
                    if (shows != null) {
                        showsResultLiveData.postValue(Resource.success(true))
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
                    showsResultLiveData.postValue(Resource.error(t.message.toString(), true))
                }
            })
        } else {
            showsResultLiveData.postValue(Resource.error("", false))
        }
    }

    fun uploadProfilePicture(
        file: File
    ) {
        changeProfilePictureResultLiveData.value = Resource.loading(true)
        if (activity.isOnline()) {
            val requestFile: RequestBody =
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
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
                        userLiveData.postValue(Resource.success(user))
                        sharedPref.edit {
                            putString(USER_IMAGE_URL_KEY, user.imageUrl)
                            putString(USER_ID_KEY, user.userId)
                            apply()
                        }
                        changeProfilePictureResultLiveData.postValue(Resource.success(true))
                    } else {
                        changeProfilePictureResultLiveData.postValue(Resource.error("", true))
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    changeProfilePictureResultLiveData.postValue(Resource.error("", true))
                }
            })
        } else {
            changeProfilePictureResultLiveData.postValue(Resource.error(NO_INTERNET_ERROR, false))
        }
    }

    fun getCurrentUser() {
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
                    userLiveData.postValue(Resource.success(user))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                userLiveData.postValue(Resource.error("", userLiveData.value?.data))
            }
        })
    }
}