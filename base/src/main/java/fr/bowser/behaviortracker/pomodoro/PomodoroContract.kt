package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroContract {

    interface Screen {

        fun updateTime(currentTime: Long)

        fun updatePomodoroTimer(timer: Timer, currentTime: Long, duration: Long)

        fun displayChoosePomodoroTimer()

        fun displayEmptyView()

        fun displayPomodoroDialog()

        fun hidePomodoroDialog()

        fun displayNoTimerAvailable()

        fun displayCreateTimerScreen()

        fun displaySettings()

        fun displayPomodoroState(isRunning: Boolean)

        fun hideDoNotDisturb()

        fun enableDoNotDisturb(enable: Boolean)

        fun displayAskDndPermission()
    }

    interface Presenter {

        val timerList: List<Timer>

        fun start()

        fun stop()

        fun isRunning(): Boolean

        fun onClickStartSession()

        fun onClickChangePomodoroState()

        fun onClickStopPomodoro()

        fun onClickCreateTimer()

        fun onClickSettings()

        fun isInstantApp(): Boolean

        fun onClickDoNotDisturb()

        fun onClickDoNotDisturbDialogOpenSettings()
    }
}