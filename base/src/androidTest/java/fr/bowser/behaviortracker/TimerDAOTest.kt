package fr.bowser.behaviortracker

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import fr.bowser.behaviortracker.database.DatabaseProvider
import fr.bowser.behaviortracker.timer.Timer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimerDAOTest {

    private lateinit var database: DatabaseProvider

    @Before
    fun initDB() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), DatabaseProvider::class.java).build()
    }

    @After
    fun closeDB() {
        database.close()
    }

    @Test
    fun addTimerTest() {
        val timer = Timer("test_timer", 0)

        val timerDAO = database.timerDAO()
        timerDAO.addTimer(timer)

        val timers = timerDAO.getTimers()
        Assert.assertTrue(timers.isNotEmpty())
        Assert.assertEquals(timer.name, timers[0].name)
    }

    @Test
    fun renameTimerTest() {
        val timer = Timer("test_timer", 0)

        val timerDAO = database.timerDAO()
        timer.id = timerDAO.addTimer(timer)

        val newName = "test_timer_2"

        timerDAO.renameTimer(timer.id, newName)

        val timers = timerDAO.getTimers()

        Assert.assertEquals(newName, timers[0].name)
    }

    @Test
    fun updateTimerTimeTest() {
        val timer = Timer("test_timer", 0)

        val timerDAO = database.timerDAO()
        timer.id = timerDAO.addTimer(timer)

        val newCurrentTime = 100L

        timerDAO.updateTimerTime(timer.id, newCurrentTime)

        val timers = timerDAO.getTimers()

        Assert.assertEquals(newCurrentTime, timers[0].time.toLong())
    }

    @Test
    fun removeTimerTest() {
        val timer = Timer("test_timer", 0)

        val timerDAO = database.timerDAO()
        timer.id = timerDAO.addTimer(timer)

        timerDAO.removeTimer(timer)

        val timers = timerDAO.getTimers()

        Assert.assertTrue(timers.isEmpty())
    }
}