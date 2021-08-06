package me.vanjavk.isa_shows_app_vanjavk.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "show")
data class ShowEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "average_rating") var averageRating: Float?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "image_url") var imageUrl: String,
    @ColumnInfo(name = "no_of_reviews") var numberOfReviews: Int,
    @ColumnInfo(name = "title") var title: String
){
    companion object {
        fun from(show: Show): ShowEntity {
            return ShowEntity(
                id = show.id,
                averageRating = show.averageRating,
                description = show.description,
                imageUrl = show.imageUrl,
                numberOfReviews = show.numberOfReviews,
                title = show.title
            )
        }
    }

    fun toShow(): Show {
        return Show(
            id = id,
            averageRating = averageRating,
            description = description,
            imageUrl = imageUrl,
            numberOfReviews = numberOfReviews,
            title = title
        )
    }
}