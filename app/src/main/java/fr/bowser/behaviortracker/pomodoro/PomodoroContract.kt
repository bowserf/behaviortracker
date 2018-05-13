package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroContract {

    interface Presenter {

        fun start()

        fun stop()

        fun onChangePomodoroStatus(actionPosition: Int, restTimerPosition: Int)

        fun onClickResetPomodoroTimer()

    }

    interface View {

        fun populateSpinnerAction(actions: List<String>)

        fun populateSpinnerRestAction(actions: List<String>)

        fun startCurrentAction()

        fun pauseCurrentAction()

        fun updatePomodoroTime(timer: Timer?, pomodoroTime: Long)

    }

}