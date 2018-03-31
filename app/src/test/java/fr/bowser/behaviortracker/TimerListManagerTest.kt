package fr.bowser.behaviortracker

import android.graphics.Color
import fr.bowser.behaviortracker.timer.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TimerListManagerTest {

    @Mock
    private lateinit var timerDAO: TimerDAO

    @Mock
    private lateinit var timeManager: TimeManager

    @Test
    fun addTimer() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        timerListManager.addTimer(timer)

        Assert.assertEquals(1, timerListManager.getTimerList().size)
        Assert.assertEquals(timer, timerListManager.getTimerList()[0])
    }

    @Test
    fun receiveAddTimerCallback() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        val timerListManagerCallback = object : TimerListManager.TimerCallback {
            override fun onTimerRemoved(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerAdded(updatedTimer: Timer) {
                Assert.assertEquals(timer, updatedTimer)
            }
        }

        timerListManager.registerTimerCallback(timerListManagerCallback)

        timerListManager.addTimer(timer)
    }

    @Test
    fun removeTimer() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        timerListManager.addTimer(timer)
        timerListManager.removeTimer(timer)

        Assert.assertEquals(0, timerListManager.getTimerList().size)
    }

    @Test
    fun receiveRemoveTimerCallback() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        val timerListManagerCallback = object : TimerListManager.TimerCallback {
            override fun onTimerRemoved(updatedTimer: Timer) {
                Assert.assertEquals(timer, updatedTimer)
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerAdded(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timerListManager.addTimer(timer)

        // only listen for remove timer callback
        timerListManager.registerTimerCallback(timerListManagerCallback)

        timerListManager.removeTimer(timer)
    }

    @Test
    fun renameTimer() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        val newName = "MyTimer2"

        timerListManager.addTimer(timer)
        timerListManager.renameTimer(timer, newName)

        Assert.assertEquals(newName, timer.name)
        Assert.assertEquals(newName, timerListManager.getTimerList()[0].name)
    }

    @Test
    fun receiveRenameTimerCallback() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)

        val newName = "MyTimer2"

        val timerListManagerCallback = object : TimerListManager.TimerCallback {
            override fun onTimerRemoved(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                Assert.assertEquals(newName, updatedTimer.name)
            }

            override fun onTimerAdded(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timerListManager.addTimer(timer)

        // only listen for remove timer callback
        timerListManager.registerTimerCallback(timerListManagerCallback)

        timerListManager.renameTimer(timer, newName)
    }

}