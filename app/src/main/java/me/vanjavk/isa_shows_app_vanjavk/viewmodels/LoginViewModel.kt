package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.models.network.LoginRequest
import me.vanjavk.isa_shows_app_vanjavk.models.network.UserResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import me.vanjavk.isa_shows_app_vanjavk.networking.Resource
import me.vanjavk.isa_shows_app_vanjavk.repository.LoginRepository
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val sharedPref: SharedPreferences,
    private val repository: LoginRepository
) : ViewModel() {
    fun getLoginResultLiveData(): LiveData<Resource<Boolean>> {
        return repository.getLoginLiveData()
    }

    fun login(email: String, password: String, rememberMe: Boolean) {
        repository.login(email, password, rememberMe)
    }
}