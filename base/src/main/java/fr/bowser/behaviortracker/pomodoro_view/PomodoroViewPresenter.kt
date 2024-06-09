package fr.bowser.behaviortracker.pomodoro_view

import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager

class PomodoroViewPresenter(
    private val screen: PomodoroViewContract.Screen,
    private val pomodoroManager: PomodoroManager,
    private val doNotDisturbManager: DoNotDisturbManager,
    private val timerRepository: TimerRepository,
) : PomodoroViewContract.Presenter {

    private val pomodoroListener = createPomodoroManagerListener()
    private val doNotDisturbListener = createDoNotDisturbManagerListener()

    private var isDialogDisplayed = false

    override fun onStart(configuration: PomodoroViewContract.Configuration) {
        pomodoroManager.addListener(pomodoroListener)
        doNotDisturbManager.addListener(doNotDisturbListener)

        if (configuration.displaySelectTimer) {
            displayPomodoroChooseTimerIfPossible()
        }

        if (pomodoroManager.currentTimer != null) {
            screen.updatePomodoroTimer(
                pomodoroManager.currentTimer!!,
                pomodoroManager.pomodoroTime,
                pomodoroManager.currentSessionDuration
            )
            updatePomodoroState()
        } else {
            screen.displayEmptyView()
        }

        if (pomodoroManager.isPendingState) {
            screen.displayPomodoroDialog()
        }

        if (doNotDisturbManager.isNotificationPolicyAccess() == DoNotDisturbManager.DnDPolicyAccess.NOT_MANAGED) {
            screen.hideDoNotDisturb()
        } else {
            updateDoNotDisturb()
        }
    }

    override fun onStop() {
        pomodoroManager.removeListener(pomodoroListener)
        doNotDisturbManager.removeListener(doNotDisturbListener)
    }

    override fun isRunning(): Boolean {
        return pomodoroManager.isStarted
    }

    override fun onClickStartSession() {
        if (pomodoroManager.isStarted) {
            return
        }

        displayPomodoroChooseTimerIfPossible()
    }

    override fun onClickChangePomodoroState() {
        if (pomodoroManager.isRunning) {
            pomodoroManager.pause()
        } else {
            pomodoroManager.resume()
        }
    }

    override fun onClickStopPomodoro() {
        pomodoroManager.stop()
    }

    override fun onClickSettings() {
        screen.displaySettings()
    }

    override fun onClickCreateTimer() {
        screen.displayCreateTimerScreen()
    }

    override fun onClickDoNotDisturb() {
        if (doNotDisturbManager.isNotificationPolicyAccess() == DoNotDisturbManager.DnDPolicyAccess.DECLINED) {
            screen.displayAskDndPermission()
            return
        }

        if (doNotDisturbManager.getDnDState() == DoNotDisturbManager.DnDState.PRIORITY) {
            doNotDisturbManager.setDnDState(DoNotDisturbManager.DnDState.ALL)
            screen.enableDoNotDisturb(false)
        } else {
            doNotDisturbManager.setDnDState(DoNotDisturbManager.DnDState.PRIORITY)
            screen.enableDoNotDisturb(true)
        }
    }

    override fun onClickDoNotDisturbDialogOpenSettings() {
        doNotDisturbManager.askPermissionIfNeeded()
    }

    private fun displayPomodoroChooseTimerIfPossible() {
        if (timerRepository.getTimerList().isEmpty()) {
            screen.displayNoTimerAvailable()
        } else {
            screen.displayPomodoroChooseTimer()
        }
    }

    private fun updatePomodoroState() {
        screen.displayPomodoroState(pomodoroManager.isRunning)
    }

    private fun updateDoNotDisturb() {
        when (doNotDisturbManager.getDnDState()) {
            DoNotDisturbManager.DnDState.ALL -> screen.enableDoNotDisturb(false)
            DoNotDisturbManager.DnDState.PRIORITY -> screen.enableDoNotDisturb(true)
            else -> {}
        }
    }

    private fun createDoNotDisturbManagerListener() = object : DoNotDisturbManager.Listener {
        override fun onDndStateChanged() {
            updateDoNotDisturb()
        }
    }

    private fun createPomodoroManagerListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {

            override fun onPomodoroSessionStarted(newTimer: Timer, duration: Long) {
                screen.updatePomodoroTimer(newTimer, duration, duration)
                updatePomodoroState()
            }

            override fun onPomodoroSessionStop() {
                isDialogDisplayed = false
                screen.displayEmptyView()
            }

            override fun onTimerStateChanged(updatedTimer: Timer) {
                if (pomodoroManager.currentTimer != updatedTimer) {
                    return
                }
                updatePomodoroState()
            }

            override fun updateTime(updatedTimer: Timer, currentTime: Long) {
                if (isDialogDisplayed) {
                    screen.hidePomodoroDialog()
                    isDialogDisplayed = false
                }
                screen.updateTime(currentTime)
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                screen.displayPomodoroDialog()
                isDialogDisplayed = true
                screen.updatePomodoroTimer(newTimer, duration, duration)
            }
        }
    }
}
