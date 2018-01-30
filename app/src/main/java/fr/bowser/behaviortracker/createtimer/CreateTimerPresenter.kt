package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class CreateTimerPresenter(private val view : CreateTimerContract.View,
                           private val timerListManager: TimerListManager) : CreateTimerContract.Presenter{

    override fun createTimer(name: String, color: Int, startNow : Boolean) {
        val timer = TimerState(startNow, Timer(name, color))
        timerListManager.addTimer(timer)

        view.exitViewAfterSucceedTimerCreation()
    }

}