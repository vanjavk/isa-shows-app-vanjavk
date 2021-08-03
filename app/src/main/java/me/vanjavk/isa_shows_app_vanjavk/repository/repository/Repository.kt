package me.vanjavk.isa_shows_app_vanjavk.repository.repository

import Show
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import me.vanjavk.isa_shows_app_vanjavk.ShowsApp
import me.vanjavk.isa_shows_app_vanjavk.USER_ID_KEY
import me.vanjavk.isa_shows_app_vanjavk.USER_IMAGE_URL_KEY
import me.vanjavk.isa_shows_app_vanjavk.isOnline
import me.vanjavk.isa_shows_app_vanjavk.models.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.models.network.ShowsResponse
import me.vanjavk.isa_shows_app_vanjavk.models.network.UserResponse
import me.vanjavk.isa_shows_app_vanjavk.modules.ApiModule
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


abstract class Repository(val activity: Activity) {
        val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val database = (activity.application as ShowsApp).showsDatabase
}
