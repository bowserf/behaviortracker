package fr.bowser.behaviortracker.timer

import android.os.Handler

class TimeManager(private val timerListManager: TimerListManager) {

    private val listeners = ArrayList<UpdateTimerCallback>()

    private val handler = Handler()

    private val timerRunnable = TimerRunnable()

    private val timerList = ArrayList<TimerState>()

    fun startTimer(timerState: TimerState): Boolean {
        if (!timerList.contains(timerState)) {
            if (timerList.isEmpty()) { // start runnable
                handler.postDelayed(timerRunnable, DELAY)
            }
            return timerList.add(timerState)
        }
        return false
    }

    fun stopTimer(timerState: TimerState) {
        timerList.remove(timerState)

        if (timerList.isEmpty()) {
            handler.removeCallbacks(timerRunnable)
        }
    }

    fun registerUpdateTimerCallback(callback: UpdateTimerCallback): Boolean {
        if (!listeners.contains(callback)) {
            return listeners.add(callback)
        }
        return false
    }

    fun unregisterUpdateTimerCallback(callback: UpdateTimerCallback) {
        listeners.remove(callback)
    }

    interface UpdateTimerCallback {
        fun timeUpdated()
    }

    inner class TimerRunnable : Runnable {
        override fun run() {
            timerList.forEach {
                timerListManager.updateTime(it, it.timer.currentTime + 1, false)
            }
            for (listener in listeners) {
                listener.timeUpdated()
            }
            handler.postDelayed(this, DELAY)
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

}