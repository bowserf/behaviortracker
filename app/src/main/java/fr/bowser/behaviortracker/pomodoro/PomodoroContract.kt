package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroContract {

    interface Presenter {

        fun start()

        fun stop()

        fun onChangePomodoroStatus(actionPosition: Int, restTimerPosition: Int)

        fun onClickResetPomodoroTimer()

        fun onItemSelectedForAction(position: Int)

        fun onItemSelectedForRest(position: Int)

    }

    interface View {

        fun populateSpinnerAction(actions: List<String>)

        fun populateSpinnerRestAction(actions: List<String>)

        fun startCurrentAction()

        fun pauseCurrentAction()

        fun updatePomodoroTime(timer: Timer?, pomodoroTime: Long)

        fun displayColorOfSelectedRestTimer(color: Int)

        fun displayColorOfSelectedActionTimer(color: Int)

        fun displayActionColorTimer(color: Int)

    }

}