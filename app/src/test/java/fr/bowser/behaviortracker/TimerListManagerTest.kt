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
    fun addTimerTest(){
        val timerListManager = TimerListManager(timerDAO, timeManager)

        val timer = Timer(1, 1000, "MyTimer", Color.RED)
        val timerState = TimerState(false, timer)

        timerListManager.addTimer(timerState)

        Assert.assertEquals(1, timerListManager.timersState.size)
        Assert.assertEquals(timer, timerListManager.timersState[0].timer)
    }

}