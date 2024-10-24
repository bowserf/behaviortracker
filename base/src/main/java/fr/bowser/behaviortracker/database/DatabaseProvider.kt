package fr.bowser.behaviortracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerDAO

@Database(entities = [(Timer::class)], version = 4)
abstract class DatabaseProvider : RoomDatabase() {

    abstract fun timerDAO(): TimerDAO
}
