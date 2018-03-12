package fr.bowser.behaviortracker.home

interface HomeContract {

    interface View {

        fun displayResetAllDialog()

        fun displaySettingsView()

    }

    interface Presenter {

        fun start()

        fun stop()

        fun onClickResetAll()

        fun onClickResetAllTimers()

        fun onClickSettings()

    }

}