package fr.bowser.behaviortracker.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.bowser.behaviortracker.timer.TimerDAO

class DatabaseManager(context: Context) {

    private val database: DatabaseProvider

    init {
        database = Room.databaseBuilder(
            context,
            DatabaseProvider::class.java,
            DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_3)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideTimerDAO(): TimerDAO {
        return database.timerDAO()
    }

    companion object {

        private const val DATABASE_NAME = "behaviortracker.db"

        val MIGRATION_1_3: Migration = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Timer ADD COLUMN position INTEGER NOT NULL DEFAULT '0'")
            }
        }
    }
}
