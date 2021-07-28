package me.vanjavk.isa_shows_app_vanjavk.database

import androidx.lifecycle.LiveData
import androidx.room.*
import me.vanjavk.isa_shows_app_vanjavk.model.ReviewEntity
import me.vanjavk.isa_shows_app_vanjavk.model.ReviewWithUser
import me.vanjavk.isa_shows_app_vanjavk.model.ShowEntity

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review")
    fun getAllShows() : LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE id IS :reviewId")
    fun getReview(reviewId: String) : LiveData<ReviewEntity>

    @Transaction
    @Query("SELECT * FROM review")
    fun getReviewsAndUsers(): LiveData<List<ReviewWithUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(shows: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShow(review: ReviewEntity)
}