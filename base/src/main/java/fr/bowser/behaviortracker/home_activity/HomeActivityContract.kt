package fr.bowser.behaviortracker.home_activity

interface HomeActivityContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun onAlarmNotificationClicked()

        fun onClickInstallApp()

        fun navigateToPomodoroScreen(displaySelectTimer: Boolean)
    }

    interface Screen {

        fun setupInstantAppButton()
    }
}
