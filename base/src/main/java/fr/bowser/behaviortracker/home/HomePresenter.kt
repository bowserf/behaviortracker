package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.instantapp.InstantAppManager

class HomePresenter(
    private val screen: HomeContract.Screen,
    private val eventManager: EventManager,
    private val instantAppManager: InstantAppManager,
    private val instantAppAddon: InstantAppAddon
) : HomeContract.Presenter {

    override fun onStart() {
        if (instantAppManager.isInstantApp()) {
            screen.setupInstantAppButton()
        }
    }

    override fun onStop() {
        // nothing to do
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
