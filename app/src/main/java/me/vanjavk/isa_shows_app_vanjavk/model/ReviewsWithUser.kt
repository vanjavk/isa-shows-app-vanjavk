package me.vanjavk.isa_shows_app_vanjavk.model

import androidx.room.Embedded
import androidx.room.Relation

data class ReviewWithUser(
    @Embedded val review: Review,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val user: User
)