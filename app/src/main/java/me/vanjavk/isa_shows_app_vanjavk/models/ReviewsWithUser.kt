package me.vanjavk.isa_shows_app_vanjavk.models

import androidx.room.Embedded
import androidx.room.Relation

data class ReviewWithUser(
    @Embedded val review: ReviewEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "id"
    )
    val user: UserEntity
)
//data class ReviewWithUser(
//    @Embedded(prefix = "xyz_") val user: UserEntity,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "user_id"
//    )
//    val review: ReviewEntity
//)