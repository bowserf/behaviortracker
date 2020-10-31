package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

class PomodoroPresenter(
    private val screen: PomodoroContract.Screen,
    private val pomodoroManager: PomodoroManager,
    override val timerList: List<Timer>,
    private val isInstantApp: Boolean
) : PomodoroContract.Presenter {

    private val pomodoroListener = createPomodoroManagerListener()

    private var isDialogDisplayed = false

    override fun start() {
        pomodoroManager.addListener(pomodoroListener)

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
    }

    override fun stop() {
        pomodoroManager.removeListener(pomodoroListener)
    }

    override fun isRunning(): Boolean {
        return pomodoroManager.isStarted
    }

    override fun onClickStartSession() {
        if (pomodoroManager.isStarted) {
            return
        }

        if (timerList.isEmpty()) {
            screen.displayNoTimerAvailable()
        } else {
            screen.displayChoosePomodoroTimer()
        }
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

    override fun isInstantApp(): Boolean {
        return isInstantApp
    }

    override fun onClickCreateTimer() {
        screen.displayCreateTimerScreen()
    }

    private fun updatePomodoroState() {
        screen.displayPomodoroState(pomodoroManager.isRunning)
    }

    private fun createPomodoroManagerListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {

            override fun onPomodoroSessionStarted(newTimer: Timer, duration: Long) {
                screen.updatePomodoroTimer(newTimer, duration, duration)
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

            override fun updateTime(timer: Timer, currentTime: Long) {
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