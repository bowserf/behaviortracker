package fr.bowser.behaviortracker.timer

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface TimerDAO {

    @Query("SELECT * FROM Timer")
    fun getTimers(): List<Timer>

    @Insert
    fun addTimer(timer: Timer): Long

    @Delete
    fun removeTimer(timer: Timer)

    @Query("UPDATE Timer SET name = :name WHERE id = :id")
    fun renameTimer(id:Long, name:String)

    @Query("UPDATE Timer SET current_time = :currentTime WHERE id = :id")
    fun updateTimerTime(id:Long, currentTime:Long)

}