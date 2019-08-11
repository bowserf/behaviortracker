package fr.bowser.behaviortracker.home

interface HomeContract {

    interface View {

        fun displayResetAllDialog()

        fun displaySettingsView()

        fun displayAlarmTimerDialog()

        fun displayRewardsView()
    }

    interface Presenter {

        fun start()

        fun stop()

        fun onClickResetAll()

        fun onClickResetAllTimers()

        fun onClickSettings()

        fun onClickAlarm()

        fun onAlarmNotificationClicked()

        fun onClickRewards()
    }
}