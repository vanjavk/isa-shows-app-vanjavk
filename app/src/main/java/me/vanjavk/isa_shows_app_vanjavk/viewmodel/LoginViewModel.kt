package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.model.network.LoginRequest
import me.vanjavk.isa_shows_app_vanjavk.model.network.LoginResponse
import me.vanjavk.isa_shows_app_vanjavk.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(var sharedPref: SharedPreferences) : ViewModel() {

    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(email: String, password: String, rememberMe: Boolean) {
        ApiModule.retrofit.login(LoginRequest(email, password)).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val accessToken = response.headers()["access-token"]
                val client = response.headers()["client"]
                val uid = response.headers()["uid"]
                if (accessToken != null && client != null && uid != null) {
                    loginResultLiveData.value = response.isSuccessful
                    with(sharedPref.edit()) {
                        putString("USER_EMAIL_KEY", email)
                        putBoolean("LOGGED_IN_KEY", rememberMe)
                        if (rememberMe){
                            putString("USER_AUTH_ACCESS_TOKEN_TYPE_KEY", accessToken)
                            putString("USER_AUTH_CLIENT_TYPE_KEY", client)
                            putString("USER_AUTH_UID_TYPE_KEY", uid)
                        }
                        apply()
                    }
                } else {
                    loginResultLiveData.value = false
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResultLiveData.value = false
            }

        })
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPref
    }
}