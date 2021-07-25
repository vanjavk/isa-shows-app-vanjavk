package me.vanjavk.isa_shows_app_vanjavk.networking

import me.vanjavk.isa_shows_app_vanjavk.model.network.*
import okhttp3.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @GET("/shows")
    fun getShows(): Call<ShowsResponse>
}
