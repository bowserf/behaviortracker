package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class CreateTimerPresenter(private val view : CreateTimerContract.View,
                           private val timerListManager: TimerListManager) : CreateTimerContract.Presenter{

    private var selectedColor:Int = 0

    override fun changeSelectedColor(oldSelectedPosition : Int, selectedPosition : Int) {
        selectedColor = selectedPosition
        view.updateColorList(oldSelectedPosition, selectedPosition)
    }

    override fun createTimer(name: String, startNow : Boolean) {
        if(name.isEmpty()){
            view.displayNameError()
            return
        }

        val timer = Timer(name, selectedColor, startNow)
        timerListManager.addTimer(timer)

        view.exitViewAfterSucceedTimerCreation()
    }

}