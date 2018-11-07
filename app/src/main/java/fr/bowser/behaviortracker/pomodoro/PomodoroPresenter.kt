package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

class PomodoroPresenter(private val screen: PomodoroContract.Screen,
                        private val pomodoroManager: PomodoroManager,
                        override val timerList: List<Timer>) : PomodoroContract.Presenter {

    private val pomodoroListener = createPomodoroManagerListener()

    private var isDialogDisplayed = false

    override fun start() {
        pomodoroManager.addListener(pomodoroListener)

        if (pomodoroManager.currentTimer != null) {
            screen.updatePomodoroTimer(
                    pomodoroManager.currentTimer!!,
                    pomodoroManager.pomodoroTime,
                    pomodoroManager.currentSessionDuration)
        }

        if (pomodoroManager.isPendingState) {
            screen.displayPomodoroDialog()
        }

        updateFabIcon()
    }

    override fun stop() {
        pomodoroManager.removeListener(pomodoroListener)
    }

    override fun isRunning(): Boolean {
        return pomodoroManager.isStarted
    }

    override fun onClickFab() {
        if (!pomodoroManager.isStarted) {
            if (timerList.isEmpty()) {
                screen.displayNoTimerAvailable()
            } else {
                screen.displayChoosePomodoroTimer()
            }
            return
        }

        if (pomodoroManager.isRunning) {
            pomodoroManager.pause()
        } else if (!pomodoroManager.isRunning) {
            pomodoroManager.resume()
        }

        updateFabIcon()
    }

    override fun onClickStopPomodoro() {
        pomodoroManager.stop()
        screen.displayEmptyView()
        updateFabIcon()
    }

    override fun onClickCreateTimer() {
        screen.displayCreateTimer()
    }

    private fun updateFabIcon() {
        if (!pomodoroManager.isStarted) {
            screen.displayStartIcon()
            return
        }

        if (pomodoroManager.isRunning) {
            screen.displayPauseIcon()
        } else {
            screen.displayPlayIcon()
        }
    }

    private fun createPomodoroManagerListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {

            override fun onPomodoroSessionStarted(newTimer: Timer, duration: Long) {
                screen.displayPauseIcon()
                screen.updatePomodoroTimer(newTimer, duration, duration)
            }

            override fun onPomodoroSessionStop() {
                isDialogDisplayed = false
                screen.displayEmptyView()
                screen.displayStartIcon()
            }

            override fun onTimerStateChanged(updatedTimer: Timer) {
                // nothing to do
            }

            override fun updateTime(timer: Timer, currentTime: Long) {
                if(isDialogDisplayed){
                    screen.dismissPomodoroDialog()
                    isDialogDisplayed = false
                }
                screen.updateTime(timer, currentTime)
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                screen.displayPomodoroDialog()
                isDialogDisplayed = true
                screen.updatePomodoroTimer(newTimer, duration, duration)
            }

        }
    }

}