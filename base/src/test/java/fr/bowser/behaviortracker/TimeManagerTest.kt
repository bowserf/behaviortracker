package fr.bowser.behaviortracker

import android.graphics.Color
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimeManagerImpl
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerDAO
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TimeManagerTest {

    @Mock
    private lateinit var timerDao: TimerDAO

    @Mock
    private lateinit var settingManager: SettingManager

    @Test
    fun startTimer() {
        val timeManager = TimeManagerImpl(timerDao, settingManager, null)

        val timerState = Timer("MyTimer", Color.RED)

        timeManager.startTimer(timerState)

        Assert.assertTrue(timerState.isActivate)
    }

    @Test
    fun startTimerListener() {
        val timeManager = TimeManagerImpl(timerDao, settingManager, null)

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

        timeManager.startTimer(timerState)

        Assert.assertTrue(result)
    }

    @Test
    fun stopTimer() {
        val timeManager = TimeManagerImpl(timerDao, settingManager, null)

        val timerState = Timer("MyTimer", Color.RED, true)

        timeManager.stopTimer(timerState)

        Assert.assertFalse(timerState.isActivate)
    }

    @Test
    fun stopTimerListener() {
        val timeManager = TimeManagerImpl(timerDao, settingManager, null)

        val timerState = Timer("MyTimer", Color.RED, true)

        var result = false

        val timerManagerListener = object : TimeManager.Listener {
            override fun onTimerStateChanged(updatedTimer: Timer) {
                result = !updatedTimer.isActivate
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timeManager.addListener(timerManagerListener)

        timeManager.stopTimer(timerState)

        Assert.assertTrue(result)
    }

    @Test
    fun stopRunningTimerWhenStartANewOne() {
        val timeManager = TimeManagerImpl(timerDao, settingManager, null)

        `when`(settingManager.isOneActiveTimerAtATime()).thenReturn(false)

        val timer1 = Timer("MyTimer1", Color.RED)
        timeManager.startTimer(timer1)

        `when`(settingManager.isOneActiveTimerAtATime()).thenReturn(true)

        val timer2 = Timer("MyTimer2", Color.BLUE)
        timeManager.startTimer(timer2)

        Assert.assertFalse(timer1.isActivate)
        Assert.assertTrue(timer2.isActivate)
    }

    @Test
    fun stopRunningTimerWhenStartANewOneListener() {
        val timeManager = TimeManagerImpl(timerDao, settingManager, null)

        `when`(settingManager.isOneActiveTimerAtATime()).thenReturn(false)

        val timer1 = Timer("MyTimer1", Color.RED)
        timeManager.startTimer(timer1)

        `when`(settingManager.isOneActiveTimerAtATime()).thenReturn(true)

        val timer2 = Timer("MyTimer2", Color.BLUE)

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

        timeManager.startTimer(timer2)

        Assert.assertTrue(timer1IsStopped && timer2IsActive)
    }
}