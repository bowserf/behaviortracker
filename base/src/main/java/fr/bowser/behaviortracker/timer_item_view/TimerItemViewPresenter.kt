package fr.bowser.behaviortracker.timer_item_view

import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.scroll_to_timer_manager.ScrollToTimerManager
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository

class TimerItemViewPresenter(
    private val screen: TimerItemViewContract.Screen,
    private val scrollToTimerManager: ScrollToTimerManager,
    private val timeManager: TimerManager,
    private val timerRepository: TimerRepository,
    private val pomodoroManager: PomodoroManager,
    private val timeProvider: TimeProvider
) : TimerItemViewContract.Presenter, TimerRepository.Listener {

    private lateinit var timer: Timer

    private val timeManagerListener = createTimeManagerListener()

    private val scrollToTimerManagerListener = createScrollToTimerManagerListener()

    override fun onStart() {
        timerRepository.addListener(this)
        timeManager.addListener(timeManagerListener)
        scrollToTimerManager.addListener(scrollToTimerManagerListener)

        screen.timerUpdated(timer.time.toLong())
        screen.statusUpdated(timer.isActivate)

        screen.updateLastUpdatedDate(
            timeProvider.convertTimestampToHumanReadable(timer.lastUpdateTimestamp)
        )
    }

    override fun onStop() {
        scrollToTimerManager.removeListener(scrollToTimerManagerListener)
        timerRepository.removeListener(this)
        timeManager.removeListener(timeManagerListener)
    }

    override fun setTimer(timer: Timer) {
        this.timer = timer
    }

    override fun onClickCard() {
        screen.startShowMode(timer.id)
    }

    override fun onClickStartPomodoro() {
        pomodoroManager.startPomodoro(timer)
    }

    override fun onClickUpdateTimer() {
        screen.displayUpdateTimerTimeDialog(timer.id)
    }

    override fun timerStateChange() {
        manageTimerUpdate()

        screen.statusUpdated(timer.isActivate)
    }

    override fun onClickResetTimer() {
        timeManager.resetTime(timer)

        screen.timerUpdated(timer.time.toLong())
    }

    override fun onClickRenameTimer() {
        screen.displayRenameDialog(timer.name)
    }

    override fun onTimerNameUpdated(newTimerName: String) {
        timerRepository.renameTimer(timer, newTimerName)
    }

    override fun onTimerAdded(updatedTimer: Timer) {
        // nothing to do
    }

    override fun onTimerRemoved(removedTimer: Timer) {
        if (timer == removedTimer) {
            timeManager.removeListener(timeManagerListener)
        }
    }

    override fun onTimerRenamed(updatedTimer: Timer) {
        if (timer == updatedTimer) {
            screen.timerRenamed(updatedTimer.name)
        }
    }

    private fun manageTimerUpdate() {
        if (!timer.isActivate) {
            timeManager.startTimer(timer)
        } else {
            timeManager.stopTimer()
        }
    }

    private fun createTimeManagerListener() = object : TimerManager.Listener {

        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                screen.statusUpdated(updatedTimer.isActivate)
                screen.updateLastUpdatedDate(
                    timeProvider.convertTimestampToHumanReadable(timer.lastUpdateTimestamp)
                )
            }
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                screen.timerUpdated(updatedTimer.time.toLong())
            }
        }
    }

    private fun createScrollToTimerManagerListener() = object: ScrollToTimerManager.Listener {
        override fun scrollToTimer(timerId: Long) {
            if(timer.id != timerId) {
                return
            }
            screen.playSelectedAnimation()
        }
    }
}
