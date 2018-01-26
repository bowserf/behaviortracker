package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerDAO

class CreateTimerManager(private val timerDAO: TimerDAO) {

    fun insertTimer(timer:Timer){
        // Can't call this on main thread
        //timerDAO.addTimer(timer)
    }

}