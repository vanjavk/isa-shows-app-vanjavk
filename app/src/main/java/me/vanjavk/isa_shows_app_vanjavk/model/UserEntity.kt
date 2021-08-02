package me.vanjavk.isa_shows_app_vanjavk.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)
