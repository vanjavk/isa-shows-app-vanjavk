package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import Show
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.database.ShowsDatabase
import me.vanjavk.isa_shows_app_vanjavk.models.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.models.network.UserResponse
import me.vanjavk.isa_shows_app_vanjavk.models.network.ShowsResponse
import me.vanjavk.isa_shows_app_vanjavk.networking.ApiModule
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.Executors


class ShowsViewModel(
    private val sharedPref: SharedPreferences,
    private val database: ShowsDatabase
) : ViewModel() {

    private val showsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val changeProfilePictureResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val userLiveData: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    private val currentUserResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<ShowEntity>> {
        return database.showDao().getAllShows()
    }

    fun getShowsResultLiveData(): LiveData<Boolean> {
        return showsResultLiveData
    }

    fun getChangeProfilePictureResultLiveDataLiveData(): LiveData<Boolean> {
        return changeProfilePictureResultLiveData
    }

    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }

    fun getCurrentUserResultLiveData(): LiveData<Boolean> {
        return currentUserResultLiveData
    }

    fun getShows() {
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
                                    it.id,
                                    it.averageRating,
                                    it.description,
                                    it.imageUrl,
                                    it.numberOfReviews,
                                    it.title
                                )
                            }
                        )
                    }
                    showsResultLiveData.value = true
                } else {
                    showsResultLiveData.value = false
                }
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                Log.d("GETSHOWSFAILURE", t.message.toString())
                showsResultLiveData.value = false
            }

        })
    }

    fun uploadProfilePicture(file: File) {
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = createFormData("image", file.name, requestFile)
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
                        putString(USER_ID_KEY, user.id)
                        apply()
                    }
                    changeProfilePictureResultLiveData.value = true
                } else {
                    changeProfilePictureResultLiveData.value = false
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("CHANGEPROFILEFAILURE", t.message.toString())
                changeProfilePictureResultLiveData.value = false
            }

        })
    }

    fun getUser() {
        ApiModule.retrofit.getCurrentUser().enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                val user = response.body()?.user
                if (user != null) {
                    with(sharedPref.edit()) {
                        putString(USER_ID_KEY, user.id)
                        apply()
                    }
                    userLiveData.value = user
                    currentUserResultLiveData.value = true
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("GETUSERFAILURE", t.message.toString())
                currentUserResultLiveData.value = false
            }
        })
    }

    fun logout() {
        with(sharedPref.edit()) {
            putBoolean(
                REMEMBER_ME_KEY,
                false
            )
            remove(USER_ID_KEY)
            remove(USER_AUTH_ACCESS_TOKEN_TYPE_KEY)
            remove(USER_AUTH_CLIENT_TYPE_KEY)
            remove(USER_AUTH_UID_TYPE_KEY)
            apply()
        }
    }


}