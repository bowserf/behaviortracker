package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class TimerPresenter(
    private val screen: TimerContract.Screen,
    private val timerListManager: TimerListManager,
    private val timeManager: TimeManager
) : TimerContract.Presenter, TimerListManager.Listener {

    private var ongoingDeletionTimer: Timer? = null

    override fun init() {
        val timers = timerListManager.getTimerList()
        screen.displayTimerList(timers)

        if (timers.isEmpty()) {
            screen.displayEmptyListView()
        } else {
            screen.displayListView()
        }
    }

    override fun start() {
        timerListManager.addListener(this)
    }

    override fun stop() {
        timerListManager.removeListener(this)
    }

    override fun onClickResetAll() {
        screen.displayResetAllDialog()
    }

    override fun onClickResetAllTimers() {
        val timers = timerListManager.getTimerList()
        timers.forEach { timer ->
            timeManager.updateTime(timer, 0f)
            timeManager.stopTimer(timer)
        }
    }

    override fun onClickSettings() {
        screen.displaySettingsView()
    }

    override fun onClickAlarm() {
        screen.displayAlarmTimerDialog()
    }

    override fun onClickRewards() {
        screen.displayRewardsView()
    }

    override fun onClickAddTimer() {
        screen.displayCreateTimerView()
    }

    override fun onTimerSwiped(timer: Timer) {
        if (ongoingDeletionTimer != null) {
            definitivelyRemoveTimer()
        }
        ongoingDeletionTimer = timer
        // simulation suppression
        onTimerRemoved(timer)
        screen.displayCancelDeletionView()
    }

    override fun definitivelyRemoveTimer() {
        if (ongoingDeletionTimer != null) {
            timerListManager.removeTimer(ongoingDeletionTimer!!)
            ongoingDeletionTimer = null
        }
    }

    override fun cancelTimerDeletion() {
        if (ongoingDeletionTimer != null) {
            onTimerAdded(ongoingDeletionTimer!!)
            ongoingDeletionTimer = null
        }
    }

    override fun onReorderFinished(timerList: List<Timer>) {
        timerListManager.reorderTimerList(timerList)
    }

    override fun onTimerRemoved(removedTimer: Timer) {
        screen.onTimerRemoved(removedTimer)
        if (screen.isTimerListEmpty()) {
            screen.displayEmptyListView()
        }
    }

    override fun onTimerAdded(updatedTimer: Timer) {
        screen.onTimerAdded(updatedTimer)
        if (!screen.isTimerListEmpty()) {
            screen.displayListView()
        }
    }

    override fun onTimerRenamed(updatedTimer: Timer) {
        // nothing to do
    }
}