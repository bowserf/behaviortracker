package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.ColorUtils

class CreateTimerPresenter(private val view: CreateTimerContract.View,
                           private val timerListManager: TimerListManager,
                           private val timeManager: TimeManager,
                           private val eventManager: EventManager) : CreateTimerContract.Presenter {

    private var colorPosition: Int = 0

    override fun changeSelectedColor(oldSelectedPosition: Int, selectedPosition: Int) {
        colorPosition = selectedPosition
        view.updateColorList(oldSelectedPosition, selectedPosition)
    }

    override fun createTimer(name: String, startNow: Boolean) {
        if (name.isEmpty()) {
            view.displayNameError()
            return
        }

        val timer = Timer(name, ColorUtils.convertPositionToColor(colorPosition))
        timerListManager.addTimer(timer)

        eventManager.sendTimerCreateEvent(startNow)

        if (startNow) {
            timeManager.startTimer(timer)
        }

        view.exitViewAfterSucceedTimerCreation()
    }

    override fun viewCreated(displayStartNow: Boolean) {
        if (!displayStartNow) {
            view.hideStartNow()
        }
    }
}