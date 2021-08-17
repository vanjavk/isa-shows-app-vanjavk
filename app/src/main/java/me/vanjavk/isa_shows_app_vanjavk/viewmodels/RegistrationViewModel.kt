package me.vanjavk.isa_shows_app_vanjavk.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.models.network.RegisterRequest
import me.vanjavk.isa_shows_app_vanjavk.models.network.RegisterResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import me.vanjavk.isa_shows_app_vanjavk.networking.Resource
import me.vanjavk.isa_shows_app_vanjavk.repository.LoginRepository
import me.vanjavk.isa_shows_app_vanjavk.repository.RegistrationRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel(
    private val repository: RegistrationRepository
) : ViewModel() {

    fun getRegistrationResultLiveData(): LiveData<Resource<Boolean>> {
        return repository.getRegistrationLiveData()
    }

    fun register(email: String, password: String) {
        repository.register(email, password)
    }
}