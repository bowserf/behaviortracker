package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class PomodoroPresenter(private val view: PomodoroContract.View,
                        private val pomodoroManager: PomodoroManager,
                        private val timerListManager: TimerListManager)
    : PomodoroContract.Presenter {

    override fun start() {
        view.populateSpinnerAction(generateActionsForSpinnerAction())
        view.populateSpinnerRestAction(generateRestActionsForSpinnerAction())

        pomodoroManager.addPomodoroCallback(pomodoroManagerCallback)

        if (!pomodoroManager.isPomodoroStarted()) {
            view.updatePomodoroTime(null, POMODORO_DURATION)
        } else {
            view.updatePomodoroTime(
                    pomodoroManager.getPomodoroCurrentTimer(),
                    pomodoroManager.getPomodoroTime())
        }

        if (pomodoroManager.isPomodoroRunning()) {
            view.startCurrentAction()
        } else {
            view.pauseCurrentAction()
        }
    }

    override fun stop() {
        pomodoroManager.removePomodoroCallback(pomodoroManagerCallback)
    }

    override fun onChangePomodoroStatus(actionPosition: Int, restTimerPosition: Int) {
        if (timerListManager.getTimerList().isEmpty()) {
            return
        }

        if (!pomodoroManager.isPomodoroStarted()) {
            val actionTimer = timerListManager.getTimerList()[actionPosition]
            pomodoroManager.startPomodoro(actionTimer,
                    POMODORO_DURATION,
                    timerListManager.getTimerList()[restTimerPosition],
                    REST_DURATION)
            view.startCurrentAction()
            view.displayActionColorTimer(actionTimer.color)
            return
        }

        if (pomodoroManager.isPomodoroRunning()) {
            pomodoroManager.pause()
            view.pauseCurrentAction()
        } else {
            pomodoroManager.resume()
            view.startCurrentAction()
        }
    }

    override fun onClickResetPomodoroTimer() {
        pomodoroManager.resetPomodoroTimer()
        view.updatePomodoroTime(
                pomodoroManager.getPomodoroCurrentTimer(),
                pomodoroManager.getPomodoroTime())
    }

    override fun onItemSelectedForAction(position: Int) {
        val timer = timerListManager.getTimerList()[position]
        view.displayColorOfSelectedActionTimer(timer.color)
    }

    override fun onItemSelectedForRest(position: Int) {
        val timer = timerListManager.getTimerList()[position]
        view.displayColorOfSelectedRestTimer(timer.color)
    }

    private fun generateActionsForSpinnerAction(): List<String> {
        val timerList = timerListManager.getTimerList()
        val spinnerActions = mutableListOf<String>()
        for (timer in timerList) {
            spinnerActions.add(timer.name)
        }
        return spinnerActions
    }

    private fun generateRestActionsForSpinnerAction(): List<String> {
        val timerList = timerListManager.getTimerList()
        val spinnerActions = mutableListOf<String>()
        for (timer in timerList) {
            spinnerActions.add(timer.name)
        }
        return spinnerActions
    }

    private val pomodoroManagerCallback = object : PomodoroManager.Callback {
        override fun onTimerStateChanged(updatedTimer: Timer) {
            //TODO
        }

        override fun updateTime(timer: Timer, currentTime: Long) {
            view.updatePomodoroTime(timer, currentTime)
        }

        override fun onCountFinished(newTimer: Timer) {
            view.displayActionColorTimer(newTimer.color)
        }

    }

    companion object {
        val POMODORO_DURATION = if(BuildConfig.DEBUG) 10L else (25 * 60).toLong()
        val REST_DURATION = if(BuildConfig.DEBUG) 5L else (5 * 60).toLong()
    }

}