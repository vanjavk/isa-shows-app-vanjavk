package me.vanjavk.isa_shows_app_vanjavk.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.vanjavk.isa_shows_app_vanjavk.models.UserEntity
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(user: List<UserEntity>)

    @Query("SELECT * FROM user WHERE id IS :userId")
    fun getUser(userId: String) : LiveData<UserEntity>
}