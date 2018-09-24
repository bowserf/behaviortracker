package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

class PomodoroPresenter(private val screen: PomodoroContract.Screen,
                        private val pomodoroManager: PomodoroManager,
                        override val timerList: List<Timer>) : PomodoroContract.Presenter {

    private val pomodoroListener = createPomodoroManagerListener()

    override fun start() {
        pomodoroManager.listener = pomodoroListener

        if (pomodoroManager.currentTimer != null) {
            screen.updatePomodoroTimer(pomodoroManager.currentTimer!!, pomodoroManager.pomodoroTime)
        }
    }

    override fun stop() {
        pomodoroManager.listener = null
    }

    override fun onClickFab() {
        screen.displayChoosePomodoroTimer()
    }

    override fun onClickStopPomodoro() {
        pomodoroManager.stop()
        screen.displayDefaultView()
    }

    private fun createPomodoroManagerListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {
            override fun onTimerStateChanged(updatedTimer: Timer) {
                // nothing to do
            }

            override fun updateTime(timer: Timer, currentTime: Long) {
                screen.updatePomodoroTime(timer, currentTime)
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                screen.updatePomodoroTimer(newTimer, duration)
            }

        }
    }

}