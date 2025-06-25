package fr.bowser.behaviortracker.home_activity

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.navigation.NavigationManager

class HomeActivityPresenter(
    private val screen: HomeActivityContract.Screen,
    private val eventManager: EventManager,
    private val navigationManager: NavigationManager,
) : HomeActivityContract.Presenter {

    override fun onStart() {
        // nothing to do
    }

    override fun onStop() {
        // nothing to do
    }

    override fun onAlarmNotificationClicked() {
        eventManager.sendAlarmNotificationClickedEvent()
    }

    override fun navigateToPomodoroScreen(displaySelectTimer: Boolean) {
        navigationManager.navigateToPomodoro(displaySelectTimer)
    }
}
