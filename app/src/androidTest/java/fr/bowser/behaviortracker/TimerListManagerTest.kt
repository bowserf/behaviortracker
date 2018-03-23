package fr.bowser.behaviortracker

import android.graphics.Color
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TimerListManagerTest {

    @Mock
    private lateinit var timerDAO: TimerDAO

    @Test
    fun addTimerTest(){
        val timerListManager = TimerListManager(timerDAO)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        timerListManager.addTimer(timerState)

        Assert.assertEquals(1, timerListManager.timersState.size)
        Assert.assertEquals(timer, timerListManager.timersState[0].timer)
    }

}