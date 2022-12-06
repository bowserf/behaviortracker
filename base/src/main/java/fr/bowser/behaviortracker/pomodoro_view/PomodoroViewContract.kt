package fr.bowser.behaviortracker.pomodoro_view

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroViewContract {

    interface Presenter {

        fun onStart(configuration: Configuration)

        fun onStop()

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

    data class Configuration(val displaySelectTimer: Boolean)
}
