package fr.bowser.behaviortracker.timer

import android.os.Handler
import fr.bowser.behaviortracker.setting.SettingManager
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

class TimeManagerImpl(private val timerDAO: TimerDAO,
                      private val settingManager: SettingManager,
                      private val handler: Handler?) : TimeManager {

    internal val background = newFixedThreadPoolContext(2, "time_manager_bg")

    private val listeners = ArrayList<TimeManager.Listener>()

    private val lastUpdatedTime = HashMap<Timer, Long>()

    private val timerRunnable = TimerRunnable()

    private val timerList = ArrayList<Timer>()

    override fun startTimer(timer: Timer) {
        if (timer.isActivate) {
            return
        }

        if (settingManager.isOneActiveTimerAtATime()) {
            stopAllRunningTimers()
        }

        lastUpdatedTime[timer] = getCurrentTimeSeconds()

        timer.isActivate = true
        for (listener in listeners) {
            listener.onTimerStateChanged(timer)
        }

        if (!timerList.contains(timer)) {
            if (timerList.isEmpty()) { // start runnable
                handler?.postDelayed(timerRunnable, DELAY)
            }
            timerList.add(timer)
        }
    }

    override fun stopTimer(timer: Timer) {
        if (!timer.isActivate) {
            return
        }

        lastUpdatedTime.remove(timer)

        timer.isActivate = false
        for (listener in listeners) {
            listener.onTimerStateChanged(timer)
        }

        timerList.remove(timer)

        if (timerList.isEmpty()) {
            handler?.removeCallbacks(timerRunnable)
        }
    }

    override fun updateTime(timer: Timer, newTime: Float, fakeTimer:Boolean) {
        var currentNewTime = newTime
        if (currentNewTime < 0) {
            currentNewTime = 0f
        }

        timer.time = currentNewTime

        if(!fakeTimer) {
            launch(background) {
                timerDAO.updateTimerTime(timer.id, timer.time.toLong())
            }
        }

        for (listener in listeners) {
            listener.onTimerTimeChanged(timer)
        }
    }

    override fun addListener(listener: TimeManager.Listener): Boolean {
        if (!listeners.contains(listener)) {
            return listeners.add(listener)
        }
        return false
    }

    override fun removeListener(listener: TimeManager.Listener) {
        listeners.remove(listener)
    }

    private fun stopAllRunningTimers() {
        for (timerToStop in timerList) {
            timerToStop.isActivate = false
            for (listener in listeners) {
                listener.onTimerStateChanged(timerToStop)
            }
        }
        timerList.clear()
        handler?.removeCallbacks(timerRunnable)
    }

    private fun getCurrentTimeSeconds(): Long {
        return System.currentTimeMillis() / 1000
    }

    inner class TimerRunnable : Runnable {
        override fun run() {
            val currentTime = getCurrentTimeSeconds()
            timerList.forEach {
                val diff = currentTime - lastUpdatedTime[it]!!
                updateTime(it, it.time + diff)
                lastUpdatedTime[it] = currentTime
            }

            handler?.postDelayed(this, DELAY)
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

}