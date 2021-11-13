package fr.bowser.behaviortracker.home

interface HomeContract {

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
