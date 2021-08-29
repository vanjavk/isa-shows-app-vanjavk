package me.vanjavk.isa_shows_app_vanjavk.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long?,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: Int,
    @ColumnInfo(name = "sync") val sync: Boolean = true,
    @Embedded val user: User
) {
    companion object {
        fun from(review: Review): ReviewEntity {
            return ReviewEntity(
                id = review.id.toLong(),
                comment = review.comment,
                rating = review.rating,
                showId = review.showId,
                user = User(
                    userId = review.user.userId,
                    email = review.user.email,
                    imageUrl = review.user.imageUrl
                )
            )
        }
    }

    fun toReview(): Review {
        return Review(
            id = id.toString(),
            comment = comment,
            rating = rating,
            showId = showId,
            user = User(
                userId = user.userId,
                email = user.email,
                imageUrl = user.imageUrl
            )
        )
    }
}