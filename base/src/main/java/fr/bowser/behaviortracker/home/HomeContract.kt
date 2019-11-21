package fr.bowser.behaviortracker.home

interface HomeContract {

    interface Screen {

        fun setupInstantAppButton()
    }

    interface Presenter {

        fun start()

        fun stop()

        fun onAlarmNotificationClicked()

        fun onClickInstallApp()
    }
}