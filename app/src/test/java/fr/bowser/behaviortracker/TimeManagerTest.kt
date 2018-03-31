package fr.bowser.behaviortracker

import android.graphics.Color
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimeManagerImpl
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerDAO
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TimeManagerTest {

    @Mock
    private lateinit var timerDao: TimerDAO

    @Test
    fun startTimer() {
        val timeManager = TimeManagerImpl(timerDao, null)

        val timerState = Timer("MyTimer", Color.RED)

        timeManager.startTimer(timerState)

        Assert.assertTrue(timerState.isActivate)
    }

    @Test
    fun startTimerCallback() {
        val timeManager = TimeManagerImpl(timerDao, null)

        val timerState = Timer("MyTimer", Color.RED)

        var result = false

        val timerManagerCallback = object : TimeManager.TimerCallback {
            override fun onTimerStateChanged(updatedTimer : Timer) {
                result = updatedTimer.isActivate
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timeManager.registerUpdateTimerCallback(timerManagerCallback)

        timeManager.startTimer(timerState)

        Assert.assertTrue(result)
    }

    @Test
    fun stopTimer() {
        val timeManager = TimeManagerImpl(timerDao, null)

        val timerState = Timer("MyTimer", Color.RED, true)

        timeManager.stopTimer(timerState)

        Assert.assertFalse(timerState.isActivate)
    }

    @Test
    fun stopTimerCallback() {
        val timeManager = TimeManagerImpl(timerDao, null)

        val timerState = Timer("MyTimer", Color.RED, true)

        var result = false

        val timerManagerCallback = object : TimeManager.TimerCallback {
            override fun onTimerStateChanged(updatedTimer: Timer) {
                result = !updatedTimer.isActivate
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timeManager.registerUpdateTimerCallback(timerManagerCallback)

        timeManager.stopTimer(timerState)

        Assert.assertTrue(result)
    }

}