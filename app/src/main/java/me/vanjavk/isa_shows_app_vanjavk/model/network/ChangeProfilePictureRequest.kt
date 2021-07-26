package me.vanjavk.isa_shows_app_vanjavk.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.vanjavk.isa_shows_app_vanjavk.model.User
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

@Serializable
data class Review(
    @SerialName("email") val email: String,
//    @Part("image")val image: MultipartBody.Part
)
