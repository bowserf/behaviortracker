package fr.bowser.behaviortracker.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerDAO

@Database(entities = [(Timer::class)], version = 2)
abstract class DatabaseProvider : RoomDatabase() {

    abstract fun timerDAO(): TimerDAO

}
