package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.timer.Timer

class PomodoroPresenter(private val screen: PomodoroContract.Screen,
                        private val pomodoroManager: PomodoroManager,
                        private val timerList: List<Timer>) : PomodoroContract.Presenter {

    private val pomodoroListener = createPomodoroManagerListener()

    override fun start() {
        pomodoroManager.listener = pomodoroListener

        if(pomodoroManager.currentTimer != null) {
            screen.updatePomodoroTimer(pomodoroManager.currentTimer!!, pomodoroManager.pomodoroTime)
        }
    }

    override fun stop() {
        pomodoroManager.listener = null
    }

    override fun onClickFab() {
        pomodoroManager.startPomodoro(
                timerList[0],
                POMODORO_DURATION,
                screen.getPauseTimer(),
                REST_DURATION)
        screen.updatePomodoroTimer(timerList[0], POMODORO_DURATION)
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

    companion object {
        val POMODORO_DURATION = if (BuildConfig.DEBUG) 10L else (25 * 60).toLong()
        val REST_DURATION = if (BuildConfig.DEBUG) 5L else (5 * 60).toLong()
    }

}