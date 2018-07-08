package fr.bowser.behaviortracker.timer

import android.os.Handler
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.utils.FakeTimer
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext

class TimeManagerImpl(private val timerDAO: TimerDAO,
                      private val settingManager: SettingManager,
                      private val handler: Handler?) : TimeManager {

    internal val background = newFixedThreadPoolContext(2, "time_manager_bg")

    private val listeners = ArrayList<TimeManager.TimerCallback>()

    private val timerRunnable = TimerRunnable()

    private val timerList = ArrayList<Timer>()

    private val lastUpdatedTime = HashMap<Long, Long>()

    override fun startTimer(timer: Timer) {
        if (timer.isActivate) {
            return
        }

        if (settingManager.isOneActiveTimerAtATime()) {
            stopAllRunningTimers()
        }

        lastUpdatedTime[timer.id] = getCurrentTimeSeconds()

        timer.isActivate = true
        for (callback in listeners) {
            callback.onTimerStateChanged(timer)
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

        lastUpdatedTime.remove(timer.id)

        timer.isActivate = false
        for (callback in listeners) {
            callback.onTimerStateChanged(timer)
        }

        timerList.remove(timer)

        if (timerList.isEmpty()) {
            handler?.removeCallbacks(timerRunnable)
        }
    }

    override fun updateTime(timer: Timer, newTime: Float) {
        var currentNewTime = newTime
        if (currentNewTime < 0) {
            currentNewTime = 0f
        }

        timer.time = currentNewTime

        if(timer.id != FakeTimer.timer.id) {
            launch(background) {
                timerDAO.updateTimerTime(timer.id, timer.time.toLong())
            }
        }

        for (callback in listeners) {
            callback.onTimerTimeChanged(timer)
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

    private fun stopAllRunningTimers() {
        for (timerToStop in timerList) {
            timerToStop.isActivate = false
            for (callback in listeners) {
                callback.onTimerStateChanged(timerToStop)
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
                val diff = currentTime - lastUpdatedTime[it.id]!!
                updateTime(it, it.time + diff)
                lastUpdatedTime[it.id] = currentTime
            }

            handler?.postDelayed(this, DELAY)
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

}