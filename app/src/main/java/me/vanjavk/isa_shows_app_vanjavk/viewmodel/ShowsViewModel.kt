package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import Show
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.USER_IMAGE_URL_KEY
import me.vanjavk.isa_shows_app_vanjavk.model.User
import me.vanjavk.isa_shows_app_vanjavk.model.network.LoginResponse
import me.vanjavk.isa_shows_app_vanjavk.model.network.ShowsResponse
import me.vanjavk.isa_shows_app_vanjavk.networking.ApiModule
import okhttp3.MediaType
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


class ShowsViewModel(var sharedPref: SharedPreferences) : ViewModel() {

    private val showsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    private val userLiveData: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun getShowsResultLiveData(): LiveData<Boolean> {
        return showsResultLiveData
    }
    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }

    fun getShows() {
        ApiModule.retrofit.getShows().enqueue(object :
            Callback<ShowsResponse> {
            override fun onResponse(
                call: Call<ShowsResponse>,
                response: Response<ShowsResponse>
            ) {
                showsLiveData.value = response.body()?.shows
                showsResultLiveData.value = true
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                showsResultLiveData.value = false
            }

        })
    }

    fun uploadProfilePicture(file: File) {
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = createFormData("image", file.name, requestFile)
        val email: RequestBody = sharedPref.getString("USER_AUTH_UID_TYPE_KEY","").orEmpty().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        ApiModule.retrofit.changeProfilePicture(email,body).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val user = response.body()?.user
                if (user!=null){
                    userLiveData.value = user
                    sharedPref.edit {
                        putString(USER_IMAGE_URL_KEY, user.imageUrl)
                        apply()
                    }
                    showsResultLiveData.value = true
                }else{
                    showsResultLiveData.value = false
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                showsResultLiveData.value = true
            }

        })
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPref
    }


}