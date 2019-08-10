package fr.bowser.behaviortracker.home

import androidx.annotation.IntDef

interface HomeManager {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SELECTED_HOME_VIEW_TIMERS_LIST, SELECTED_HOME_VIEW_POMODORO)
    annotation class SelectedHomeView

    fun setCurrentView(@SelectedHomeView viewId: Int)

    @SelectedHomeView
    fun getCurrentView(): Int

    companion object {
        const val SELECTED_HOME_VIEW_TIMERS_LIST = 0
        const val SELECTED_HOME_VIEW_POMODORO = 1
    }
}