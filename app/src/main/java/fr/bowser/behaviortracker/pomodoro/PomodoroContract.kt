package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroContract {

    interface Screen {

        fun updatePomodoroTime(timer: Timer, currentTime: Long)

        fun updatePomodoroTimer(timer: Timer, duration: Long)

        fun displayChoosePomodoroTimer()

        fun displayDefaultView()

    }

    interface Presenter {

        val timerList: List<Timer>

        fun start()

        fun stop()

        fun onClickFab()

        fun onClickStopPomodoro()

    }

}