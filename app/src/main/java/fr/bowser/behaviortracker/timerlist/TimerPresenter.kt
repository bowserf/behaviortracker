package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class TimerPresenter(private val view: TimerContract.View,
                     private val timerListManager: TimerListManager,
                     private val timerNotificationManager: TimerNotificationManager)
    : TimerContract.Presenter, TimerListManager.TimerCallback {

    override fun start() {
        timerListManager.registerTimerCallback(this)
        val timers = timerListManager.timersState
        view.displayTimerList(timers)

        if(timers.isEmpty()){
            view.displayEmptyListView()
        }else {
            view.displayListView()
        }
    }

    override fun stop() {
        timerListManager.unregisterTimerCallback(this)
    }

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

    override fun onTimerRemoved(updatedTimerState: TimerState, position:Int) {
        if(timerListManager.timersState.isEmpty()){
            view.displayEmptyListView()
        }
        view.onTimerRemoved(updatedTimerState, position)
    }

    override fun onTimerAdded(updatedTimerState: TimerState, position: Int) {
        if(!timerListManager.timersState.isEmpty()){
            view.displayListView()
        }
        // if timer is directly activate, display it in the notification
        if(updatedTimerState.isActivate){
            timerNotificationManager.displayTimerNotif(updatedTimerState)
        }
        view.onTimerAdded(updatedTimerState, position)
    }

    override fun onTimerStateChanged(updatedTimerState: TimerState, position: Int) {
        // nothing to do
    }

    override fun onTimerTimeChanged(updatedTimerState: TimerState, position: Int) {
        // nothing to do
    }

    override fun onTimerRenamed(updatedTimerState: TimerState, position: Int) {
        // nothing to do
    }
}