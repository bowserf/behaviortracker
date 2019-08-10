package fr.bowser.behaviortracker.home

import fr.bowser.behaviortracker.home.HomeManager.Companion.SELECTED_HOME_VIEW_TIMERS_LIST

class HomeManagerImpl : HomeManager {

    private var selectedViewId: Int

    init {
        selectedViewId = SELECTED_HOME_VIEW_TIMERS_LIST
    }

    override fun setCurrentView(viewId: Int) {
        selectedViewId = viewId
    }

    override fun getCurrentView(): Int {
        return selectedViewId
    }
}