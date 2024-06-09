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

    private var runningTimerJob: Job? = null

    private var lastUpdatedTime = 0L

    private var startedTimer: Timer? = null

    private var isRunning = false

    override fun startTimer(timer: Timer, fakeTimer: Boolean) {
        if (isRunning(timer)) {
            return
        }

        stopStartedTimer()

        lastUpdatedTime = getCurrentTimeSeconds()

        updateLastUpdateTimestamp(timer, fakeTimer)

        setTimerActivateState(timer, true)

        runningTimerJob = coroutineScope.launch {
            while (true) {
                delay(DELAY)
                val currentTime = getCurrentTimeSeconds()
                val diff = currentTime - lastUpdatedTime
                withContext(Dispatchers.Main) {
                    updateTime(timer, timer.time + diff)
                }
                lastUpdatedTime = currentTime
            }
        }

        this.startedTimer = timer

        addOn.onTimerStarted()
    }

    override fun stopTimer(fakeTimer: Boolean) {
        val timer = startedTimer ?: return

        if (!isRunning) {
            return
        }

        updateLastUpdateTimestamp(timer, fakeTimer)

        stopStartedTimer()
    }

    override fun isRunning(timer: Timer): Boolean {
        return startedTimer == timer && isRunning
    }

    override fun getStartedTimer(): Timer? {
        return startedTimer
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

    override fun resetTime(timer: Timer, fakeTimer: Boolean) {
        timer.time = 0f
        if (!fakeTimer) {
            coroutineScope.launch {
                timerDAO.updateTimerTime(timer.id, timer.time.toLong())
            }
        }

        updateLastUpdateTimestamp(timer, fakeTimer)

        for (listener in listeners) {
            listener.onTimerTimeChanged(timer)
            listener.onTimerStateChanged(timer)
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
        val startedTimer = startedTimer ?: return

        this.startedTimer = null

        runningTimerJob?.cancel()

        // reset value to be consistent
        lastUpdatedTime = 0L

        setTimerActivateState(startedTimer, false)
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

    private fun setTimerActivateState(timer: Timer, isActivated: Boolean) {
        isRunning = isActivated
        for (listener in listeners) {
            listener.onTimerStateChanged(timer)
        }
    }

    interface AddOn {
        fun onTimerStarted()
    }

    companion object {
        private const val DELAY = 1000L
    }
}
