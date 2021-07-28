package me.vanjavk.isa_shows_app_vanjavk.database

import me.vanjavk.isa_shows_app_vanjavk.model.ShowEntity
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShowDao {

    @Query("SELECT * FROM show ORDER BY show.id")
    fun getAllShows() : LiveData<List<ShowEntity>>

    @Query("SELECT * FROM show WHERE id IS :showId")
    fun getShow(showId: String) : LiveData<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShows(shows: List<ShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShow(show: ShowEntity)

}