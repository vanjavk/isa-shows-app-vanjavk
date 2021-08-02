package me.vanjavk.isa_shows_app_vanjavk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? ,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: Int,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "sync") val sync: Boolean = true
)