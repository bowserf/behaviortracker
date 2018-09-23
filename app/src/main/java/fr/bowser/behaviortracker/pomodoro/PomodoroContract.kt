package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroContract {

    interface Screen {

        fun getPauseTimer(): Timer

        fun updatePomodoroTime(timer: Timer, currentTime: Long)

        fun updatePomodoroTimer(timer: Timer, duration: Long)

    }

    interface Presenter {

        fun start()

        fun stop()

        fun onClickFab()

    }

}