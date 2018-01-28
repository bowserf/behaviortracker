package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class CreateTimerPresenter(private val view : CreateTimerContract.View,
                           private val timerListManager: TimerListManager) : CreateTimerContract.Presenter{

    override fun createTimer(name: String, color: Int) {
        val timer = Timer(name, color)
        timerListManager.addTimer(timer)

        view.exitViewAfterSucceedTimerCreation()
    }

}