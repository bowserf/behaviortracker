package fr.bowser.behaviortracker.timer_item_view

import fr.bowser.behaviortracker.navigation.NavigationManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.scroll_to_timer_manager.ScrollToTimerManager
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository

class TimerItemViewPresenter(
    private val screen: TimerItemViewContract.Screen,
    private val navigationManager: NavigationManager,
    private val pomodoroManager: PomodoroManager,
    private val scrollToTimerManager: ScrollToTimerManager,
    private val timerManager: TimerManager,
    private val timeProvider: TimeProvider,
    private val timerRepository: TimerRepository,
) : TimerItemViewContract.Presenter, TimerRepository.Listener {

    private lateinit var timer: Timer

    private val timeManagerListener = createTimeManagerListener()

    private val scrollToTimerManagerListener = createScrollToTimerManagerListener()

    override fun onStart() {
        timerRepository.addListener(this)
        timerManager.addListener(timeManagerListener)
        scrollToTimerManager.addListener(scrollToTimerManagerListener)

        screen.setTime(timer.time.toLong())
        updateTimerStatus()

        screen.updateLastUpdatedDate(
            timeProvider.convertTimestampToHumanReadable(timer.lastUpdateTimestamp)
        )
    }

    override fun onStop() {
        scrollToTimerManager.removeListener(scrollToTimerManagerListener)
        timerRepository.removeListener(this)
        timerManager.removeListener(timeManagerListener)
    }

    override fun setTimer(timer: Timer) {
        this.timer = timer
        updateTimerStatus()
        screen.setTime(timer.time.toLong())
        screen.setColorId(timer.colorId)
        screen.setName(timer.name)
    }

    override fun onClickCard() {
        screen.startShowMode(timer.id)
    }

    override fun onClickStartPomodoro() {
        pomodoroManager.startPomodoro(timer)
        navigationManager.navigateToPomodoro(false)
    }

    override fun onClickUpdateTimer() {
        screen.displayUpdateTimerTimeDialog(timer.id)
    }

    override fun timerStateChange() {
        manageTimerUpdate()
        updateTimerStatus()
    }

    override fun onClickResetTimer() {
        timerManager.resetTime(timer)

        screen.setTime(timer.time.toLong())
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
            timerManager.removeListener(timeManagerListener)
        }
    }

    override fun onTimerRenamed(updatedTimer: Timer) {
        if (timer == updatedTimer) {
            screen.setName(updatedTimer.name)
        }
    }

    private fun manageTimerUpdate() {
        if (!timerManager.isRunning(timer)) {
            timerManager.startTimer(timer)
        } else {
            timerManager.stopTimer()
        }
    }

    private fun createTimeManagerListener() = object : TimerManager.Listener {

        override fun onTimerStateChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                updateTimerStatus()
                screen.updateLastUpdatedDate(
                    timeProvider.convertTimestampToHumanReadable(timer.lastUpdateTimestamp)
                )
            }
        }

        override fun onTimerTimeChanged(updatedTimer: Timer) {
            if (timer == updatedTimer) {
                screen.setTime(updatedTimer.time.toLong())
            }
        }
    }

    private fun updateTimerStatus() {
        screen.statusUpdated(timerManager.isRunning(timer))
    }

    private fun createScrollToTimerManagerListener() = object : ScrollToTimerManager.Listener {
        override fun scrollToTimer(timerId: Long) {
            if (timer.id != timerId) {
                return
            }
            screen.playSelectedAnimation()
        }
    }
}
