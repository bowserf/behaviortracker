package fr.bowser.behaviortracker.home_activity

interface HomeActivityContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun onAlarmNotificationClicked()

        fun onClickInstallApp()
    }

    interface Screen {

        fun setupInstantAppButton()
    }
}
