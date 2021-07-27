package me.vanjavk.isa_shows_app_vanjavk.database

import me.vanjavk.isa_shows_app_vanjavk.model.ShowEntity
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowDao {

    @Query("SELECT * FROM show")
    fun getAllShows() : LiveData<List<ShowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<ShowEntity>)

}