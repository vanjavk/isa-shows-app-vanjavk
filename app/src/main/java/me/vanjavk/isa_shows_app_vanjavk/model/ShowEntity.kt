package me.vanjavk.isa_shows_app_vanjavk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "show")
data class ShowEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "average_rating") var averageRating: Float?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "image_url") var imageUrl: String,
    @ColumnInfo(name = "no_of_reviews") var numberOfReviews: Int,
    @ColumnInfo(name = "title") var title: String
)