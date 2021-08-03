package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.models.network.LoginRequest
import me.vanjavk.isa_shows_app_vanjavk.models.network.UserResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import me.vanjavk.isa_shows_app_vanjavk.repository.repository.Repository
import me.vanjavk.isa_shows_app_vanjavk.repository.repository.ShowsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val sharedPref: SharedPreferences
) : ViewModel() {

    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(email: String, password: String, rememberMe: Boolean) {
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
                    loginResultLiveData.value = response.isSuccessful
                    with(sharedPref.edit()) {
                        putString(USER_ID_KEY, user.id)
                        putString(USER_EMAIL_KEY, email)
                        putString(USER_IMAGE_URL_KEY, user.imageUrl)
                        putBoolean(REMEMBER_ME_KEY, rememberMe)
                        if (rememberMe) {
                            putString(USER_AUTH_ACCESS_TOKEN_TYPE_KEY, accessToken)
                            putString(USER_AUTH_CLIENT_TYPE_KEY, client)
                            putString(USER_AUTH_UID_TYPE_KEY, uid)
                        }
                        apply()
                    }
                } else {
                    loginResultLiveData.value = false
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                loginResultLiveData.value = false
            }

        })
    }
}