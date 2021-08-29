package me.vanjavk.isa_shows_app_vanjavk.repository

import android.app.Activity
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.models.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.models.network.LoginRequest
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

class LoginRepository(activity: Activity) : Repository(activity) {

    private val loginLiveData = MutableLiveData<Resource<Boolean>>()

    fun getLoginLiveData(): MutableLiveData<Resource<Boolean>> = loginLiveData

    fun login(email: String, password: String, rememberMe: Boolean) {
        loginLiveData.value = Resource.loading(true)
        if (activity.isOnline()) {
            ApiModule.retrofit.login(LoginRequest(email, password)).enqueue(object :
                Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val accessToken = response.headers()["access-token"]
                    val client = response.headers()["client"]
                    val uid = response.headers()["uid"]
                    val user = response.body()?.user
                    if (accessToken != null && client != null && uid != null && user != null) {
                        with(sharedPref.edit()) {
                            putString(USER_ID_KEY, user.userId)
                            putString(USER_EMAIL_KEY, email)
                            putString(USER_IMAGE_URL_KEY, user.imageUrl)
                            putBoolean(REMEMBER_ME_KEY, rememberMe)
                            putString(USER_AUTH_ACCESS_TOKEN_TYPE_KEY, accessToken)
                            putString(USER_AUTH_CLIENT_TYPE_KEY, client)
                            putString(USER_AUTH_UID_TYPE_KEY, uid)

                            apply()
                        }
                        loginLiveData.postValue(Resource.success(true))
                    } else {
                        loginLiveData.postValue(Resource.error("", true))
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    loginLiveData.postValue(Resource.error("", true))
                }

            })
        } else {
            loginLiveData.postValue(Resource.error("", false))
        }


    }
}