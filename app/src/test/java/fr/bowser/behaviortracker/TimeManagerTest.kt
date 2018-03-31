package fr.bowser.behaviortracker

import android.graphics.Color
import fr.bowser.behaviortracker.timer.*
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

        val timerState = TimerState(false, Timer(1, 1000000, "MyTimer", Color.RED))

        timeManager.startTimer(timerState)

        Assert.assertTrue(timerState.isActivate)
    }

    @Test
    fun startTimerCallback() {
        val timeManager = TimeManagerImpl(timerDao, null)

        val timerState = TimerState(false, Timer(1, 1000000, "MyTimer", Color.RED))

        var result = false

        val timerManagerCallback = object : TimeManager.TimerCallback {
            override fun onTimerStateChanged(updatedTimerState: TimerState) {
                result = updatedTimerState.isActivate
            }

            override fun onTimerTimeChanged(updatedTimerState: TimerState) {
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

        val timerState = TimerState(true, Timer(1, 1000000, "MyTimer", Color.RED))

        timeManager.stopTimer(timerState)

        Assert.assertFalse(timerState.isActivate)
    }

    @Test
    fun stopTimerCallback() {
        val timeManager = TimeManagerImpl(timerDao, null)

        val timerState = TimerState(true, Timer(1, 1000000, "MyTimer", Color.RED))

        var result = false

        val timerManagerCallback = object : TimeManager.TimerCallback {
            override fun onTimerStateChanged(updatedTimerState: TimerState) {
                result = !updatedTimerState.isActivate
            }

            override fun onTimerTimeChanged(updatedTimerState: TimerState) {
                Assert.assertTrue(false)
            }
        }

        timeManager.registerUpdateTimerCallback(timerManagerCallback)

        timeManager.stopTimer(timerState)

        Assert.assertTrue(result)
    }

}