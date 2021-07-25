package me.vanjavk.isa_shows_app_vanjavk.networking

import me.vanjavk.isa_shows_app_vanjavk.model.network.LoginRequest
import me.vanjavk.isa_shows_app_vanjavk.model.network.LoginResponse
import me.vanjavk.isa_shows_app_vanjavk.model.network.RegisterRequest
import me.vanjavk.isa_shows_app_vanjavk.model.network.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
