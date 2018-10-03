package fr.bowser.behaviortracker.timer

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext


class TimerListManagerImpl(private val timerDAO: TimerDAO,
                           private val timeManager: TimeManager) : TimerListManager {

    internal val background = newFixedThreadPoolContext(2, "bg")

    private val timers = ArrayList<Timer>()

    private val listeners = ArrayList<TimerListManager.Listener>()

    init {
        launch(background) {
            timers.addAll(timerDAO.getTimers())
        }
    }

    override fun addTimer(timer: Timer) {
        timer.position = timers.size

        timers.add(timer)

        for (listener in listeners) {
            listener.onTimerAdded(timer)
        }

        launch(background) {
            timer.id = timerDAO.addTimer(timer)
        }
    }

    override fun removeTimer(oldTimer: Timer) {
        timeManager.stopTimer(oldTimer)

        timers.remove(oldTimer)
        for (listener in listeners) {
            listener.onTimerRemoved(oldTimer)
        }

        launch(background) {
            timerDAO.removeTimer(oldTimer)

            timers.sortBy { timer -> timer.position }
            reorderTimerList(timers)
        }
    }

    override fun reorderTimerList(timerList: List<Timer>) {
        for (i in 0 until timerList.size) {
            val timer = timerList[i]
            timer.position = i
        }

        launch(background) {
            for (timer in timerList) {
                timerDAO.updateTimerPosition(timer.id, timer.position)
            }
        }
    }

    override fun getTimerList(): List<Timer> {
        return timers
    }

    override fun renameTimer(timer: Timer, newName: String) {
        timer.name = newName
        launch(background) {
            timerDAO.renameTimer(timer.id, newName)
        }

        for (listener in listeners) {
            listener.onTimerRenamed(timer)
        }
    }

    override fun addListener(listener: TimerListManager.Listener): Boolean {
        if (listeners.contains(listener)) {
            return false
        }
        return listeners.add(listener)
    }

    override fun removeListener(listener: TimerListManager.Listener): Boolean {
        return listeners.remove(listener)
    }

}