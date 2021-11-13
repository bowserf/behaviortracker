package fr.bowser.behaviortracker.home_view

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
