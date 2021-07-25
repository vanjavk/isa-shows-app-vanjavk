package me.vanjavk.isa_shows_app_vanjavk.viewmodel

import Show
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.vanjavk.isa_shows_app_vanjavk.model.network.LoginResponse
import me.vanjavk.isa_shows_app_vanjavk.model.network.ShowsResponse
import me.vanjavk.isa_shows_app_vanjavk.networking.ApiModule
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ShowsViewModel(var sharedPref: SharedPreferences) : ViewModel() {

    private val showsResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getShowsResultLiveData(): LiveData<Boolean> {
        return showsResultLiveData
    }

    fun getShows() {
        ApiModule.retrofit.getShows().enqueue(object :
            Callback<ShowsResponse> {
            override fun onResponse(
                call: Call<ShowsResponse>,
                response: Response<ShowsResponse>
            ) {
                showsResultLiveData.value = true
            }
            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                showsResultLiveData.value = true
            }

        })
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPref
    }

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    private val userProfilePictureLiveData: MutableLiveData<File> by lazy {
        MutableLiveData<File>()
    }

    fun getUserProfilePictureLiveData(): LiveData<File> {
        return userProfilePictureLiveData
    }

    fun initShows() {
//        showsLiveData.value = shows
    }

    fun setImage(file: File) {
        userProfilePictureLiveData.value = file
    }

}