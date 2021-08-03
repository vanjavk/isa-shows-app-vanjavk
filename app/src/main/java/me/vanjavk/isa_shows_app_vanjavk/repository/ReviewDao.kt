package me.vanjavk.isa_shows_app_vanjavk.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import me.vanjavk.isa_shows_app_vanjavk.models.ReviewEntity
import me.vanjavk.isa_shows_app_vanjavk.models.ReviewWithUser

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review")
    fun getAllShows() : LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE id IS :reviewId")
    fun getReview(reviewId: String) : LiveData<ReviewEntity>

    @Transaction
    @Query("SELECT * FROM review WHERE show_id IS :showId ORDER BY review.id DESC")
    fun getReviewsAndUsers(showId: String): LiveData<List<ReviewWithUser>>

    @Transaction
    @Query("SELECT * FROM review WHERE review.sync IS 0 AND show_id IS :showId")
    fun getOfflineReviews(showId: String):List<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllReviews(shows: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReview(review: ReviewEntity)

    @Query("DELETE FROM review WHERE review.id IS :reviewId")
    fun removeReview(reviewId: Long)
}