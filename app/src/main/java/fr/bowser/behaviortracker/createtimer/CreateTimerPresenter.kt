package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class CreateTimerPresenter(private val view : CreateTimerContract.View,
                           private val timerListManager: TimerListManager) : CreateTimerContract.Presenter{

    var selectedColor:Int = 0
        private set

    override fun changeSelectedColor(color: Int, oldSelectedPosition : Int, selectedPosition : Int) {
        selectedColor = color
        view.updateColorList(oldSelectedPosition, selectedPosition)
    }

    override fun createTimer(name: String, color: Int, startNow : Boolean) {
        if(name.isEmpty()){
            view.displayNameError()
            return
        }

        val timer = TimerState(startNow, Timer(name, color))
        timerListManager.addTimer(timer)

        view.exitViewAfterSucceedTimerCreation()
    }

}