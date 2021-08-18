package me.vanjavk.isa_shows_app_vanjavk.repository

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import me.vanjavk.isa_shows_app_vanjavk.isOnline
import me.vanjavk.isa_shows_app_vanjavk.models.network.RegisterRequest
import me.vanjavk.isa_shows_app_vanjavk.models.network.RegisterResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
import me.vanjavk.isa_shows_app_vanjavk.networking.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationRepository(activity: Activity) : Repository(activity) {

    private val registrationLiveData = MutableLiveData<Resource<Boolean>>()

    fun getRegistrationLiveData(): MutableLiveData<Resource<Boolean>> = registrationLiveData

    fun register(email: String, password: String) {
        registrationLiveData.value = Resource.loading(true)
        if (activity.isOnline()) {
            ApiModule.retrofit.register(RegisterRequest(email, password, password)).enqueue(object :
                Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.code()==201){
                        registrationLiveData.postValue(Resource.success(true))
                    }else{
                        registrationLiveData.postValue(Resource.error("", true))
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registrationLiveData.postValue(Resource.error("", true))
                }

            })
        } else {
            registrationLiveData.postValue(Resource.error("", false))
        }

    }
}