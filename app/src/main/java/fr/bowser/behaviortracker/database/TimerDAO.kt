package fr.bowser.behaviortracker.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import fr.bowser.behaviortracker.model.Timer

@Dao
interface TimerDAO {

    @Query("SELECT * FROM Timer")
    abstract fun getCategories(): List<Timer>

    @Insert
    abstract fun addTimer(timer: Timer)

}