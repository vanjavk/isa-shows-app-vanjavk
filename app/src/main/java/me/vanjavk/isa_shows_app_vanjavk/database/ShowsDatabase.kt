package me.vanjavk.isa_shows_app_vanjavk.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.vanjavk.isa_shows_app_vanjavk.model.ReviewEntity
import me.vanjavk.isa_shows_app_vanjavk.model.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ReviewEntity::class,
        ShowEntity::class
    ],
    version = 10
)
abstract class ShowsDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: ShowsDatabase? = null

        fun getDatabase(context: Context): ShowsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context,
                    ShowsDatabase::class.java,
                    "shows_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = database
                database
            }
        }
    }

    abstract fun showDao(): ShowDao
    abstract fun reviewDao(): ReviewDao
    abstract fun userDao(): UserDao
}