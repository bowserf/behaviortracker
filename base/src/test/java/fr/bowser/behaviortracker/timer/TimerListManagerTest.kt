package fr.bowser.behaviortracker.timer

import android.graphics.Color
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.timer_repository.TimerRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class timerRepositoryTest {

    @Mock
    private lateinit var timerDAO: TimerDAO

    @Mock
    private lateinit var timeManager: TimerManager

    @Test
    fun addTimer() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer = Timer("MyTimer", Color.RED)

        timerRepository.addTimer(timer)

        Assert.assertEquals(1, timerRepository.getTimerList().size)
        Assert.assertEquals(timer, timerRepository.getTimerList()[0])
    }

    @Test
    fun receiveAddTimerListener() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer = Timer("MyTimer", Color.RED)

        var result = false

        val timerRepositoryListener = object : TimerRepository.Listener {
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

        timerRepository.addListener(timerRepositoryListener)

        timerRepository.addTimer(timer)

        Assert.assertTrue(result)
    }

    @Test
    fun removeTimer() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer = Timer("MyTimer", Color.RED)

        timerRepository.addTimer(timer)
        timerRepository.removeTimer(timer)

        Assert.assertEquals(0, timerRepository.getTimerList().size)
    }

    @Test
    fun receiveRemoveTimerListener() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer = Timer("MyTimer", Color.RED)

        var result = false

        val timerRepositoryListener = object : TimerRepository.Listener {
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

        timerRepository.addTimer(timer)

        // only listen to remove timer listener
        timerRepository.addListener(timerRepositoryListener)

        timerRepository.removeTimer(timer)

        Assert.assertTrue(result)
    }

    @Test
    fun renameTimer() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer = Timer("MyTimer", Color.RED)

        val newName = "MyTimer2"

        timerRepository.addTimer(timer)
        timerRepository.renameTimer(timer, newName)

        Assert.assertEquals(newName, timer.name)
        Assert.assertEquals(newName, timerRepository.getTimerList()[0].name)
    }

    @Test
    fun receiveRenameTimerListener() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer = Timer("MyTimer", Color.RED)

        val newName = "MyTimer2"

        var result = false

        val timerRepositoryListener = object : TimerRepository.Listener {
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

        timerRepository.addTimer(timer)

        // only listen to remove timer listener
        timerRepository.addListener(timerRepositoryListener)

        timerRepository.renameTimer(timer, newName)

        Assert.assertTrue(result)
    }

    @Test
    fun reorderTimerList() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer1 = Timer("Timer1", Color.RED)
        val timer2 = Timer("Timer2", Color.RED)
        val timer3 = Timer("Timer3", Color.RED)

        timerRepository.addTimer(timer1)
        timerRepository.addTimer(timer2)
        timerRepository.addTimer(timer3)

        val reorderList = mutableListOf(timer3, timer2, timer1)

        timerRepository.reorderTimerList(reorderList)

        Assert.assertEquals(0, timer3.position)
        Assert.assertEquals(1, timer2.position)
        Assert.assertEquals(2, timer1.position)
    }

    @Test
    fun reorderTimerListChangeInternalList() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer1 = Timer("Timer1", Color.RED)
        val timer2 = Timer("Timer2", Color.RED)
        val timer3 = Timer("Timer3", Color.RED)

        timerRepository.addTimer(timer1)
        timerRepository.addTimer(timer2)
        timerRepository.addTimer(timer3)

        val reorderList = mutableListOf(timer3, timer2, timer1)

        timerRepository.reorderTimerList(reorderList)

        val timerList = timerRepository.getTimerList()
        Assert.assertEquals(timer3, timerList[0])
        Assert.assertEquals(timer2, timerList[1])
        Assert.assertEquals(timer1, timerList[2])
    }

    @Test
    fun setPositionWhenAddTimer() = runTest {
        val timerRepository = createTimerRepositoryImpl(backgroundScope)

        val timer1 = Timer("Timer1", Color.RED, position = -1)
        timerRepository.addTimer(timer1)

        Assert.assertEquals(0, timer1.position)
    }

    private fun createTimerRepositoryImpl(backgroundScope: CoroutineScope): TimerRepository {
        return TimerRepositoryImpl(backgroundScope, timerDAO, timeManager)
    }
}
