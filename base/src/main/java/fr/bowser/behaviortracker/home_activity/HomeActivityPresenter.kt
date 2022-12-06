package fr.bowser.behaviortracker.home_activity

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.instantapp.InstantAppManager

class HomeActivityPresenter(
    private val screen: HomeActivityContract.Screen,
    private val eventManager: EventManager,
    private val instantAppManager: InstantAppManager,
    private val instantAppAddon: InstantAppAddon
) : HomeActivityContract.Presenter {

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
