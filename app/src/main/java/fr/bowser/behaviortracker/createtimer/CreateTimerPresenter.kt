package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.timer.Timer

class CreateTimerPresenter(private val view : CreateTimerContract.View,
                           private val manager: CreateTimerManager) : CreateTimerContract.Presenter{

    override fun createTimer(name: String, color: Int) {
        val timer = Timer(name, color)
        manager.insertTimer(timer)

        view.exitViewAfterSucceedTimerCreation()
    }

}