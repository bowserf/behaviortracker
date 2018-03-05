package fr.bowser.behaviortracker.timer

import android.os.Handler
import java.util.*

class TimeManager(private val timerListManager: TimerListManager) {

    private val listeners = ArrayList<UpdateTimerCallback>()

    private val handler = Handler()

    private val timerRunnable = TimerRunnable()

    private var isTimerRunning = false

    fun registerUpdateTimerCallback(callback: UpdateTimerCallback): Boolean {
        if (!listeners.contains(callback)) {
            // start the runnable if we will put the first listener
            if (!isTimerRunning) {
                val numberActiveTimers = timerListManager.timersState
                        .filter { it.isActivate }
                        .size
                if (numberActiveTimers > 0) {
                    isTimerRunning = true
                    handler.postDelayed(timerRunnable, DELAY)
                }
            }
            return listeners.add(callback)
        }
        return false
    }

    fun unregisterUpdateTimerCallback(callback: UpdateTimerCallback) {
        listeners.remove(callback)

        // stop runnable if there is no listener anymore
        if (isTimerRunning) {
            val numberActiveTimers = timerListManager.timersState
                    .filter { it.isActivate }
                    .size
            if (numberActiveTimers == 0) {
                isTimerRunning = false
                handler.removeCallbacks(timerRunnable)
            }
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

    interface UpdateTimerCallback {

        fun timeUpdated()

    }

    inner class TimerRunnable : Runnable {
        override fun run() {
            timerListManager.timersState
                    .filter { it.isActivate }
                    .forEach {
                        timerListManager.updateTime(it, it.timer.currentTime + 1, false)
                    }
            for (listener in listeners) {
                listener.timeUpdated()
            }
            handler.postDelayed(this, DELAY)
        }
    }

}