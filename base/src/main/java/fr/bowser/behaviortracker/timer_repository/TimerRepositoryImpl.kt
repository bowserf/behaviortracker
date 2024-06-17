package fr.bowser.behaviortracker.timer_repository

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerDAO
import fr.bowser.behaviortracker.timer.TimerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val timerDAO: TimerDAO,
    private val timeManager: TimerManager,
) : TimerRepository {

    private val timers = ArrayList<Timer>()

    private val listeners = ArrayList<TimerRepository.Listener>()

    init {
        coroutineScope.launch {
            timers.addAll(timerDAO.getTimers())
        }
    }

    override fun addTimer(timer: Timer) {
        timer.position = timers.size

        timers.add(timer)

        for (listener in listeners) {
            listener.onTimerAdded(timer)
        }

        coroutineScope.launch {
            timer.id = timerDAO.addTimer(timer)
        }
    }

    override fun removeTimer(timer: Timer) {
        if (timeManager.getStartedTimer() == timer) {
            timeManager.stopTimer()
        }

        timers.remove(timer)
        for (listener in listeners) {
            listener.onTimerRemoved(timer)
        }

        coroutineScope.launch {
            timerDAO.removeTimer(timer)
            reorderTimerList(timers)
        }
    }

    override fun removeAllTimers() {
        coroutineScope.launch {
            timerDAO.removeAllTimers()
            withContext(Dispatchers.Main) {
                timeManager.stopTimer()
                timers.toList().forEach { oldTimer ->
                    timers.remove(oldTimer)
                    for (listener in listeners) {
                        listener.onTimerRemoved(oldTimer)
                    }
                }
            }
        }
    }

    override fun reorderTimerList(timerList: List<Timer>) {
        for (i in timerList.indices) {
            val timer = timerList[i]
            timer.position = i
        }

        timers.sortBy { timer -> timer.position }

        coroutineScope.launch {
            for (timer in timerList) {
                timerDAO.updateTimerPosition(timer.id, timer.position)
            }
        }
    }

    override fun getTimerList(): List<Timer> {
        return timers
    }

    override fun getTimer(timerId: Long): Timer {
        return timers.first { it.id == timerId }
    }

    override fun renameTimer(timer: Timer, newName: String) {
        timer.name = newName
        coroutineScope.launch {
            timerDAO.renameTimer(timer.id, newName)
        }

        for (listener in listeners) {
            listener.onTimerRenamed(timer)
        }
    }

    override fun addListener(listener: TimerRepository.Listener): Boolean {
        if (listeners.contains(listener)) {
            return false
        }
        return listeners.add(listener)
    }

    override fun removeListener(listener: TimerRepository.Listener): Boolean {
        return listeners.remove(listener)
    }
}
