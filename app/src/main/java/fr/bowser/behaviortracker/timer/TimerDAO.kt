package fr.bowser.behaviortracker.timer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimerDAO {

    @Query("SELECT * FROM Timer ORDER BY position")
    fun getTimers(): List<Timer>

    @Insert
    fun addTimer(timer: Timer): Long

    @Delete
    fun removeTimer(timer: Timer)

    @Query("UPDATE Timer SET name = :name WHERE id = :id")
    fun renameTimer(id: Long, name: String)

    @Query("UPDATE Timer SET current_time = :currentTime WHERE id = :id")
    fun updateTimerTime(id: Long, currentTime: Long)

    @Query("UPDATE Timer SET position = :position WHERE id = :id")
    fun updateTimerPosition(id: Long, position: Int)

}