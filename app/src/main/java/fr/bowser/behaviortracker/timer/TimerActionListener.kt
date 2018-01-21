package fr.bowser.behaviortracker.timer

import fr.bowser.behaviortracker.model.Timer

interface TimerActionListener {

    fun onTimerStateChange(timer: Timer)
    fun onClickIncreaseTime(timer: Timer)
    fun onClickDecreaseTime(timer: Timer)
    
}