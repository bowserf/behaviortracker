package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroContract {

    interface Screen {

        fun updateTime(timer: Timer, currentTime: Long)

        fun updatePomodoroTimer(timer: Timer, currentTime: Long, duration: Long)

        fun displayChoosePomodoroTimer()

        fun displayEmptyView()

        fun displayPauseIcon()

        fun displayPlayIcon()

        fun displayStartIcon()

        fun displayPomodoroDialog()

        fun displayNoTimerAvailable()

        fun displayCreateTimer()

        fun dismissPomodoroDialog()

        fun displaySettings()
    }

    interface Presenter {

        val timerList: List<Timer>

        fun start()

        fun stop()

        fun isRunning(): Boolean

        fun onClickFab()

        fun onClickStopPomodoro()

        fun onClickCreateTimer()

        fun onClickSettings()

        fun isInstantApp(): Boolean
    }
}