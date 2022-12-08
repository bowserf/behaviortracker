package fr.bowser.behaviortracker.timer

import fr.bowser.behaviortracker.time_provider.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerManagerImpl(
    private val coroutineScope: CoroutineScope,
    private val timerDAO: TimerDAO,
    private val timeProvider: TimeProvider,
    private val addOn: AddOn
) : TimerManager {

    private val listeners = ArrayList<TimerManager.Listener>()

    private val lastUpdatedTime = HashMap<Timer, Long>()

    private var runningTimerJob: Job? = null

    private var timer: Timer? = null

    override fun startTimer(timer: Timer, fakeTimer: Boolean) {
        if (timer.isActivate) {
            return
        }

        stopStartedTimer()

        lastUpdatedTime[timer] = getCurrentTimeSeconds()

        updateLastUpdateTimestamp(timer, fakeTimer)

        timer.isActivate = true
        for (listener in listeners) {
            listener.onTimerStateChanged(timer)
        }

        runningTimerJob = coroutineScope.launch {
            while (true) {
                delay(DELAY)
                val currentTime = getCurrentTimeSeconds()
                val diff = currentTime - lastUpdatedTime[timer]!!
                withContext(Dispatchers.Main) {
                    updateTime(timer, timer.time + diff)
                }
                lastUpdatedTime[timer] = currentTime
            }
        }

        this.timer = timer

        addOn.onTimerStarted()
    }

    override fun stopTimer(fakeTimer: Boolean) {
        val timer = timer ?: return

        if (!timer.isActivate) {
            return
        }

        lastUpdatedTime.remove(timer)

        updateLastUpdateTimestamp(timer, fakeTimer)

        stopStartedTimer()
    }

    override fun getStartedTimer(): Timer? {
        return timer
    }

    override fun updateTime(timer: Timer, newTime: Float, fakeTimer: Boolean) {
        var currentNewTime = newTime
        if (currentNewTime < 0) {
            currentNewTime = 0f
        }

        timer.time = currentNewTime

        if (!fakeTimer) {
            coroutineScope.launch {
                timerDAO.updateTimerTime(timer.id, timer.time.toLong())
            }
        }

        for (listener in listeners) {
            listener.onTimerTimeChanged(timer)
        }
    }

    override fun addListener(listener: TimerManager.Listener): Boolean {
        if (!listeners.contains(listener)) {
            return listeners.add(listener)
        }
        return false
    }

    override fun removeListener(listener: TimerManager.Listener) {
        listeners.remove(listener)
    }

    private fun stopStartedTimer() {
        val startedTimer = timer ?: return
        startedTimer.isActivate = false
        for (listener in listeners) {
            listener.onTimerStateChanged(startedTimer)
        }

        this.timer = null

        runningTimerJob?.cancel()
    }

    private fun getCurrentTimeSeconds(): Long {
        return timeProvider.getCurrentTimeMs() / 1000
    }

    private fun updateLastUpdateTimestamp(
        timer: Timer,
        fakeTimer: Boolean
    ) {
        val currentTime = timeProvider.getCurrentTimeMs()
        timer.lastUpdateTimestamp = currentTime
        if (!fakeTimer) {
            coroutineScope.launch {
                timerDAO.updateLastUpdatedTimestamp(timer.id, currentTime)
            }
        }
    }

    interface AddOn {
        fun onTimerStarted()
    }

    companion object {
        private const val TAG = "TimeManagerImpl"
        private const val DELAY = 1000L
    }
}
