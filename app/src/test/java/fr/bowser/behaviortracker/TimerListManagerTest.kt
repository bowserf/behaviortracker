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
        val timerListManager = TimerListManager(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        timerListManager.addTimer(timerState)

        Assert.assertEquals(1, timerListManager.timersState.size)
        Assert.assertEquals(timer, timerListManager.timersState[0].timer)
    }

    @Test
    fun receiveAddTimerCallback() {
        val timerListManager = TimerListManager(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        val timerListManagerCallback = object : TimerListManager.TimerCallback {
            override fun onTimerRemoved(updatedTimerState: TimerState) {
                Assert.assertTrue(false)
            }

            override fun onTimerRenamed(updatedTimerState: TimerState) {
                Assert.assertTrue(false)
            }

            override fun onTimerAdded(updatedTimerState: TimerState) {
                Assert.assertEquals(timerState, updatedTimerState)
            }
        }

        timerListManager.registerTimerCallback(timerListManagerCallback)

        timerListManager.addTimer(timerState)
    }

    @Test
    fun removeTimer() {
        val timerListManager = TimerListManager(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        timerListManager.addTimer(timerState)
        timerListManager.removeTimer(timerState)

        Assert.assertEquals(0, timerListManager.timersState.size)
    }

    @Test
    fun receiveRemoveTimerCallback() {
        val timerListManager = TimerListManager(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        val timerListManagerCallback = object : TimerListManager.TimerCallback {
            override fun onTimerRemoved(updatedTimerState: TimerState) {
                Assert.assertEquals(timerState, updatedTimerState)
            }

            override fun onTimerRenamed(updatedTimerState: TimerState) {
                Assert.assertTrue(false)
            }

            override fun onTimerAdded(updatedTimerState: TimerState) {
                Assert.assertTrue(false)
            }
        }

        timerListManager.addTimer(timerState)

        // only listen for remove timer callback
        timerListManager.registerTimerCallback(timerListManagerCallback)

        timerListManager.removeTimer(timerState)
    }

    @Test
    fun renameTimer() {
        val timerListManager = TimerListManager(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        val newName = "MyTimer2"

        timerListManager.addTimer(timerState)
        timerListManager.renameTimer(timerState, newName)

        Assert.assertEquals(newName, timerState.timer.name)
        Assert.assertEquals(newName, timerListManager.timersState[0].timer.name)
    }

    @Test
    fun receiveRenameTimerCallback() {
        val timerListManager = TimerListManager(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        val newName = "MyTimer2"

        val timerListManagerCallback = object : TimerListManager.TimerCallback {
            override fun onTimerRemoved(updatedTimerState: TimerState) {
                Assert.assertTrue(false)
            }

            override fun onTimerRenamed(updatedTimerState: TimerState) {
                Assert.assertEquals(newName, updatedTimerState.timer.name)
            }

            override fun onTimerAdded(updatedTimerState: TimerState) {
                Assert.assertTrue(false)
            }
        }

        timerListManager.addTimer(timerState)

        // only listen for remove timer callback
        timerListManager.registerTimerCallback(timerListManagerCallback)

        timerListManager.renameTimer(timerState, newName)
    }

}