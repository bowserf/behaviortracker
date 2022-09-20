package fr.bowser.behaviortracker.home_view

import fr.bowser.behaviortracker.event.EventManager

class HomePresenter(
    private val screen: HomeContract.Screen,
    private val eventManager: EventManager
) : HomeContract.Presenter {

    override fun onStart() {
        // nothing to do
    }

    override fun onStop() {
        // nothing to do
    }

    override fun onAlarmNotificationClicked() {
        eventManager.sendAlarmNotificationClickedEvent()
    }
}
