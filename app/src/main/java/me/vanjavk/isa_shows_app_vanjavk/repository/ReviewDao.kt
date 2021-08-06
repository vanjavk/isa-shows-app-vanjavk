package me.vanjavk.isa_shows_app_vanjavk.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import me.vanjavk.isa_shows_app_vanjavk.models.Review
import me.vanjavk.isa_shows_app_vanjavk.models.ReviewEntity

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE show_id IS :showId ORDER BY review.id DESC LIMIT 50 ")
    fun getReviews(showId : String) : LiveData<List<ReviewEntity>>

    @Query("SELECT * FROM review WHERE userId IS :reviewId")
    fun getReview(reviewId: String) : LiveData<ReviewEntity>

//    @Transaction
//    @Query("SELECT * FROM review WHERE show_id IS :showId ORDER BY review.userId DESC")
//    fun getReviewsAndUsers(showId: String): LiveData<List<ReviewWithUser>>

    @Transaction
    @Query("SELECT * FROM review WHERE review.sync IS 0")
    fun getOfflineReviews():List<ReviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReviews(shows: List<ReviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReview(review: ReviewEntity)

    @Transaction
    fun removeReviews(reviews: List<ReviewEntity>){
        reviews.forEach { it.id?.let { id -> removeReview(id) } }
    }

    @Query("DELETE FROM review WHERE review.id IS :reviewId")
    fun removeReview(reviewId: Long)
}