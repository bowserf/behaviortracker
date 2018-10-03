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
    fun receiveAddTimerListener() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        var result = false

        val timerListManagerListener = object : TimerListManager.Listener {
            override fun onTimerRemoved(removedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerAdded(updatedTimer: Timer) {
                result = timer == updatedTimer
            }
        }

        timerListManager.addListener(timerListManagerListener)

        timerListManager.addTimer(timer)

        Assert.assertTrue(result)
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
    fun receiveRemoveTimerListener() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        var result = false

        val timerListManagerListener = object : TimerListManager.Listener {
            override fun onTimerRemoved(removedTimer: Timer) {
                result = timer == removedTimer
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerAdded(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timerListManager.addTimer(timer)

        // only listen for remove timer listener
        timerListManager.addListener(timerListManagerListener)

        timerListManager.removeTimer(timer)

        Assert.assertTrue(result)
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
    fun receiveRenameTimerListener() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer = Timer("MyTimer", Color.RED)

        val newName = "MyTimer2"

        var result = false

        val timerListManagerListener = object : TimerListManager.Listener {
            override fun onTimerRemoved(removedTimer: Timer) {
                Assert.assertTrue(false)
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                result = newName == updatedTimer.name
            }

            override fun onTimerAdded(updatedTimer: Timer) {
                Assert.assertTrue(false)
            }
        }

        timerListManager.addTimer(timer)

        // only listen for remove timer listener
        timerListManager.addListener(timerListManagerListener)

        timerListManager.renameTimer(timer, newName)

        Assert.assertTrue(result)
    }

    @Test
    fun reorderTimerList() {
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer1 = Timer("Timer1", Color.RED)
        val timer2 = Timer("Timer2", Color.RED)
        val timer3 = Timer("Timer3", Color.RED)

        timerListManager.addTimer(timer1)
        timerListManager.addTimer(timer2)
        timerListManager.addTimer(timer3)

        val reorderList = mutableListOf(timer3, timer2, timer1)

        timerListManager.reorderTimerList(reorderList)

        Assert.assertEquals(0, timer3.position)
        Assert.assertEquals(1, timer2.position)
        Assert.assertEquals(2, timer1.position)
    }

    @Test
    fun setPositionWhenAddTimer(){
        val timerListManager = TimerListManagerImpl(timerDAO, timeManager)

        val timer1 = Timer("Timer1", Color.RED, false, -1)
        timerListManager.addTimer(timer1)

        Assert.assertEquals(0, timer1.position)
    }

}