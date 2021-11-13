package fr.bowser.behaviortracker.timer_list_section_view

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_list.TimerListManager

class TimerSectionPresenter(
    private val screen: TimerSectionContract.Screen,
    private val timerListManager: TimerListManager,
    private val timeManager: TimeManager
) : TimerSectionContract.Presenter {

    private var isTimersActivated = false

    private var ongoingDeletionTimer: Timer? = null

    private val timeManagerListener = createTimeManagerListener()
    private val timerListManagerListener = createTimerListManagerListener()

    override fun onResume() {
        timeManager.addListener(timeManagerListener)
        timerListManager.addListener(timerListManagerListener)

        updateTimerList()
    }

    override fun onPause() {
        timeManager.removeListener(timeManagerListener)
        timerListManager.removeListener(timerListManagerListener)
    }

    override fun populate(title: String, isActive: Boolean) {
        this.isTimersActivated = isActive
        screen.updateTitle(title)
        updateTimerList()
    }

    override fun onTimerSwiped(timerPosition: Int) {
        ongoingDeletionTimer = timerListManager.getTimerList().first {
            it.position == timerPosition
        }
        timerListManager.removeTimer(ongoingDeletionTimer!!)
        screen.displayCancelDeletionView(CANCEL_TIMER_REMOVAL_DURATION)
        updateTimerList()
    }

    override fun onClickCancelTimerDeletion() {
        if (ongoingDeletionTimer == null) {
            return
        }
        timerListManager.addTimer(ongoingDeletionTimer!!)
        ongoingDeletionTimer = null
        updateTimerList()
    }

    private fun updateTimerList() {
        val timers = timerListManager.getTimerList().filter {
            it.isActivate == isTimersActivated
        }.filter {
            it != ongoingDeletionTimer
        }
        screen.updateTimerList(timers)
        screen.displayTitle(timers.isNotEmpty())
    }

    private fun createTimeManagerListener() = object : TimeManager.Listener {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            updateTimerList()
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            // nothing to do
        }
    }

    private fun createTimerListManagerListener() = object : TimerListManager.Listener {
        override fun onTimerRemoved(removedTimer: Timer) {
            updateTimerList()
        }

        override fun onTimerAdded(updatedTimer: Timer) {
            updateTimerList()
        }

        override fun onTimerRenamed(updatedTimer: Timer) {
            // nothing to do
        }
    }

    companion object {
        private const val CANCEL_TIMER_REMOVAL_DURATION = 3_000
    }
}
