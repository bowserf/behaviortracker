package fr.bowser.behaviortracker.pomodoro

interface PomodoroDialogContract {

    interface Screen

    interface Presenter {

        fun onClickPositionButton()

        fun onClickNegativeButton()

    }

}