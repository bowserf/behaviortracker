package fr.bowser.behaviortracker.database


import android.arch.persistence.room.Room
import android.content.Context

class DatabaseManager(context: Context) {

    private val database: DatabaseProvider

    init {
        database = Room.databaseBuilder(context,
                DatabaseProvider::class.java,
                DATABASE_NAME)
                .build()
    }

    fun provideTimerDAO(): TimerDAO {
        return database.timerDAO()
    }

    companion object {

        private val DATABASE_NAME = "behaviortracker.db"
    }

}
