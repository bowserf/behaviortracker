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

        fun populateSpinnerAction(actions: MutableList<String>)

        fun populateSpinnerRest(rest: MutableList<String>)

        fun startCurrentAction()

        fun pauseCurrentAction()

        fun updatePomodoroTime(timer: Timer?, pomodoroTime: Long)

        fun displayColorOfSelectedRestTimer(colorIndex: Int)

        fun displayColorOfSelectedActionTimer(colorIndex: Int)

        fun displayActionColorTimer(colorIndex: Int)

        fun displayColorNoAction()

        fun displayColorNoRest()

        fun createTimer()

        fun selectActionTimer(positionNewTimer: Int)

        fun selectRestTimer(position: Int)

        fun noActionTimerSelected()

        fun sameTimerIsSelectedForBothRoles()

    }

}