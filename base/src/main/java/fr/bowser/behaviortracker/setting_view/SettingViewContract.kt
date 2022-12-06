package fr.bowser.behaviortracker.setting_view

interface SettingViewContract {

    interface Presenter {
        fun onStart()
        fun onStop()
    }

    interface Screen
}
