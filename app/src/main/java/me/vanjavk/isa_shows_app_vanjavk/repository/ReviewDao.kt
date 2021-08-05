package me.vanjavk.isa_shows_app_vanjavk.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import me.vanjavk.isa_shows_app_vanjavk.models.ReviewEntity

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review LIMIT 50")
    fun getAllReviews() : LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE userId IS :reviewId")
    fun getReview(reviewId: String) : LiveData<ReviewEntity>

//    @Transaction
//    @Query("SELECT * FROM review WHERE show_id IS :showId ORDER BY review.userId DESC")
//    fun getReviewsAndUsers(showId: String): LiveData<List<ReviewWithUser>>

//    @Transaction
//    @Query("SELECT * FROM review WHERE review.sync IS 0 AND show_id IS :showId")
//    fun getOfflineReviews(showId: String):List<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(shows: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReview(review: ReviewEntity)

    @Query("DELETE FROM review WHERE review.userId IS :reviewId")
    fun removeReview(reviewId: Long)
}