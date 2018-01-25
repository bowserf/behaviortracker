package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.model.Timer

interface TimerActionListener {

    fun onTimerStateChange(timer: Timer)
    fun onClickIncreaseTime(timer: Timer)
    fun onClickDecreaseTime(timer: Timer)
    
}