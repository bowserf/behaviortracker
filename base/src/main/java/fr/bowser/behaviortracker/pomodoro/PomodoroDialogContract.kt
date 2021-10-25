package fr.bowser.behaviortracker.pomodoro

interface PomodoroDialogContract {

    interface Presenter {

        fun onClickPositionButton()

        fun onClickNegativeButton()
    }

    interface Screen
}