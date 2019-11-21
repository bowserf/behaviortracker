package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.notification.TimerNotificationManager

class HomePresenter(
        private val screen: HomeContract.Screen,
        private val timerNotificationManager: TimerNotificationManager,
        private val eventManager: EventManager,
        private val instantAppManager: InstantAppManager,
        private val instantAppAddon: InstantAppAddon
) : HomeContract.Presenter {

    override fun start() {
        timerNotificationManager.changeOngoingState(false)

        if(instantAppManager.isInstantApp()) {
            screen.setupInstantAppButton()
        }
    }

    override fun stop() {
        timerNotificationManager.changeOngoingState(true)
    }

    override fun onAlarmNotificationClicked() {
        eventManager.sendAlarmNotificationClickedEvent()
    }

    override fun onClickInstallApp() {
        instantAppAddon.displayInstallAppDialog()
    }

    interface InstantAppAddon {

        fun displayInstallAppDialog()
    }
}