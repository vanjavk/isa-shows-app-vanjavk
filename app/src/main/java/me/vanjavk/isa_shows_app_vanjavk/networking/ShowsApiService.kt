package me.vanjavk.isa_shows_app_vanjavk.networking

import me.vanjavk.isa_shows_app_vanjavk.model.network.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    @GET("/shows")
    fun getShows(): Call<ShowsResponse>
    @GET("/shows/{id}")
    fun getShow(@Path("id") id: String): Call<ShowResponse>
    @GET("/shows/{id}/reviews")
    fun getReviews(@Path("id") id: String): Call<ReviewsResponse>
    @POST("/reviews")
    fun addReview(@Body request: AddReviewRequest): Call<AddReviewResponse>
    @Multipart
    @PUT("/users")
    fun changeProfilePicture(@Part("email") email: RequestBody,
                             @Part image: MultipartBody.Part): Call<LoginResponse>


}
