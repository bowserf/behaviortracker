package fr.bowser.behaviortracker.timer

import android.os.Handler
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

class TimeManagerImpl(private val timerDAO: TimerDAO) : TimeManager{

    internal val background = newFixedThreadPoolContext(2, "time_manager_bg")

    private val listeners = ArrayList<TimeManager.TimerCallback>()

    private val handler = Handler()

    private val timerRunnable = TimerRunnable()

    private val timerList = ArrayList<TimerState>()

    override fun startTimer(timerState: TimerState) {
        if (timerState.isActivate) {
            return
        }

        timerState.isActivate = true
        for (callback in listeners) {
            callback.onTimerStateChanged(timerState)
        }

        if (!timerList.contains(timerState)) {
            if (timerList.isEmpty()) { // start runnable
                handler.postDelayed(timerRunnable, DELAY)
            }
            timerList.add(timerState)
        }
    }

    override fun stopTimer(timerState: TimerState) {
        if (!timerState.isActivate) {
            return
        }

        timerState.isActivate = false
        for (callback in listeners) {
            callback.onTimerStateChanged(timerState)
        }

        timerList.remove(timerState)

        if (timerList.isEmpty()) {
            handler.removeCallbacks(timerRunnable)
        }
    }


    override fun updateTime(timerState: TimerState, newTime: Long) {
        var currentNewTime = newTime
        if (currentNewTime < 0) {
            currentNewTime = 0
        }

        timerState.timer.currentTime = currentNewTime

        launch(background) {
            timerDAO.updateTimerTime(timerState.timer.id, timerState.timer.currentTime)
        }

        for (callback in listeners) {
            callback.onTimerTimeChanged(timerState)
        }
    }

    override fun registerUpdateTimerCallback(callback: TimeManager.TimerCallback): Boolean {
        if (!listeners.contains(callback)) {
            return listeners.add(callback)
        }
        return false
    }

    override fun unregisterUpdateTimerCallback(callback: TimeManager.TimerCallback) {
        listeners.remove(callback)
    }

    inner class TimerRunnable : Runnable {
        override fun run() {
            timerList.forEach {
                updateTime(it, it.timer.currentTime + 1)
            }

            handler.postDelayed(this, DELAY)
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

}