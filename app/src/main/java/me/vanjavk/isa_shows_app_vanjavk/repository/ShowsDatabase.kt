package me.vanjavk.isa_shows_app_vanjavk.repository
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.vanjavk.isa_shows_app_vanjavk.models.ReviewEntity
import me.vanjavk.isa_shows_app_vanjavk.models.ShowEntity
import me.vanjavk.isa_shows_app_vanjavk.models.UserEntity
import me.vanjavk.isa_shows_app_vanjavk.repository.database.ReviewDao
import me.vanjavk.isa_shows_app_vanjavk.repository.database.ShowDao
import me.vanjavk.isa_shows_app_vanjavk.repository.database.UserDao

@Database(
    entities = [
        UserEntity::class,
        ReviewEntity::class,
        ShowEntity::class
    ],
    version = 19
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