package fr.bowser.behaviortracker.home_activity

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.navigation.NavigationManager

class HomeActivityPresenter(
    private val screen: HomeActivityContract.Screen,
    private val eventManager: EventManager,
    private val instantAppManager: InstantAppManager,
    private val instantAppAddon: InstantAppAddon,
    private val navigationManager: NavigationManager,
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

    override fun navigateToPomodoroScreen(displaySelectTimer: Boolean) {
        navigationManager.navigateToPomodoro(displaySelectTimer)
    }

    interface InstantAppAddon {

        fun displayInstallAppDialog()
    }
}
