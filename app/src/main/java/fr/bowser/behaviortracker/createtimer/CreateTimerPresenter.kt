package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.timer.TimerState

class CreateTimerPresenter(private val view : CreateTimerContract.View,
                           private val timerListManager: TimerListManager) : CreateTimerContract.Presenter{

    var selectedColor:Int = 0
        private set

    override fun changeSelectedColor(oldSelectedPosition : Int, selectedPosition : Int) {
        selectedColor = selectedPosition
        view.updateColorList(oldSelectedPosition, selectedPosition)
    }

    override fun createTimer(name: String, startNow : Boolean) {
        if(name.isEmpty()){
            view.displayNameError()
            return
        }

        val timer = TimerState(startNow, Timer(name, selectedColor))
        timerListManager.addTimer(timer)

        view.exitViewAfterSucceedTimerCreation()
    }

}