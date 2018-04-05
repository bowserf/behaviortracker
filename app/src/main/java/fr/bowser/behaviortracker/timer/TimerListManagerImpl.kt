package fr.bowser.behaviortracker.timer

import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext


class TimerListManagerImpl(private val timerDAO: TimerDAO,
                           private val timeManager: TimeManager) : TimerListManager {

    internal val background = newFixedThreadPoolContext(2, "bg")

    private val timers = ArrayList<Timer>()

    private val callbacks = ArrayList<TimerListManager.TimerCallback>()

    init {
        launch(background) {
            timers.addAll(timerDAO.getTimers())
        }
    }

    override fun addTimer(timer: Timer) {
        timer.position = timers.size

        timers.add(timer)

        for (callback in callbacks) {
            callback.onTimerAdded(timer)
        }

        launch(background) {
            timer.id = timerDAO.addTimer(timer)
        }
    }

    override fun removeTimer(timer: Timer) {
        timeManager.stopTimer(timer)

        timers.remove(timer)
        for (callback in callbacks) {
            callback.onTimerRemoved(timer)
        }

        launch(background) {
            timerDAO.removeTimer(timer)
        }
    }

    override fun reorderTimerList(timerList : List<Timer>){
        launch(background) {
            for (i in 0 until timerList.size) {
                val timer = timerList[i]
                timer.position = i
                timerDAO.updateTimerPosition(timer.id, i)
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

        for (callback in callbacks) {
            callback.onTimerRenamed(timer)
        }
    }

    override fun registerTimerCallback(timerCallback: TimerListManager.TimerCallback): Boolean {
        if (callbacks.contains(timerCallback)) {
            return false
        }
        return callbacks.add(timerCallback)
    }

    override fun unregisterTimerCallback(timerCallback: TimerListManager.TimerCallback): Boolean {
        return callbacks.remove(timerCallback)
    }

}