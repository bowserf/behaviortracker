package fr.bowser.behaviortracker.timer

import android.graphics.Color
import fr.bowser.behaviortracker.time.TimeProvider
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

    @Mock
    private lateinit var timeProvider: TimeProvider

    @Mock
    private lateinit var addOn: TimeManagerImpl.AddOn

    @Test
    fun startTimer() {
        // Given
        val timeManager = TimeManagerImpl(timerDao, timeProvider, null, addOn = addOn)
        val timerState = Timer("MyTimer", Color.RED)

        // When
        timeManager.startTimer(timerState)

        // Given
        Assert.assertTrue(timerState.isActivate)
    }

    @Test
    fun startTimerListener() {
        // Given
        val timeManager = TimeManagerImpl(timerDao, timeProvider, null, addOn = addOn)

        val timerState = Timer("MyTimer", Color.RED)

        var result = false

        val timerManagerListener = object : TimeManager.Listener {
            override fun onTimerStateChanged(updatedTimer: Timer) {
                result = updatedTimer.isActivate
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timeManager.addListener(timerManagerListener)

        // When
        timeManager.startTimer(timerState)

        // Then
        Assert.assertTrue(result)
    }

    @Test
    fun stopTimer() {
        // Given
        val timeManager = TimeManagerImpl(timerDao, timeProvider, null, addOn = addOn)
        val timer = Timer("MyTimer", Color.RED)
        timeManager.startTimer(timer)

        // When
        timeManager.stopTimer()

        // Then
        Assert.assertFalse(timer.isActivate)
    }

    @Test
    fun stopTimerListener() {
        // Given
        val timeManager = TimeManagerImpl(timerDao, timeProvider, null, addOn = addOn)

        val timer = Timer("MyTimer", Color.RED)

        var result = false

        timeManager.startTimer(timer)

        val timerManagerListener = object : TimeManager.Listener {
            override fun onTimerStateChanged(updatedTimer: Timer) {
                result = !updatedTimer.isActivate
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timeManager.addListener(timerManagerListener)

        // When
        timeManager.stopTimer()

        // Then
        Assert.assertTrue(result)
    }

    @Test
    fun stopRunningTimerWhenStartANewOne() {
        // Given
        val timeManager = TimeManagerImpl(timerDao, timeProvider, null, addOn = addOn)
        val timer1 = Timer("MyTimer1", Color.RED)
        val timer2 = Timer("MyTimer2", Color.BLUE)
        timeManager.startTimer(timer1)

        // When
        timeManager.startTimer(timer2)

        // Then
        Assert.assertFalse(timer1.isActivate)
        Assert.assertTrue(timer2.isActivate)
    }

    @Test
    fun stopRunningTimerWhenStartANewOneListener() {
        // Given
        val timeManager = TimeManagerImpl(timerDao, timeProvider, null, addOn = addOn)
        val timer1 = Timer("MyTimer1", Color.RED)
        val timer2 = Timer("MyTimer2", Color.BLUE)

        timeManager.startTimer(timer1)

        var timer1IsStopped = false
        var timer2IsActive = false

        val timerManagerListener = object : TimeManager.Listener {
            override fun onTimerStateChanged(updatedTimer: Timer) {
                if (updatedTimer == timer1 && !updatedTimer.isActivate) {
                    timer1IsStopped = true
                }
                if (updatedTimer == timer2 && updatedTimer.isActivate) {
                    timer2IsActive = true
                }
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }
        timeManager.addListener(timerManagerListener)

        // When
        timeManager.startTimer(timer2)

        // Then
        Assert.assertTrue(timer1IsStopped && timer2IsActive)
    }
}