package fr.bowser.behaviortracker.timer

import android.graphics.Color


class TimerListManager(private val timerDAO: TimerDAO) {

    val timers = ArrayList<TimerState>()

    private val callbacks = ArrayList<TimerCallback>()

    init{
        timers.add(TimerState(false, Timer(1, 0, "Work", Color.RED)))
        timers.add(TimerState(false, Timer(2, 0, "Break", Color.BLUE)))
        timers.add(TimerState(false, Timer(3, 0, "Lunch", Color.GREEN)))
    }

    fun addTimer(timer:TimerState){
        timers.add(timer)
        for (callback in callbacks) {
            callback.onTimerAdded(timer)
        }
    }

    fun removeTimer(timer:TimerState){
        timers.remove(timer)
        for (callback in callbacks) {
            callback.onTimerRemoved(timer)
        }
    }

    fun registerTimerCallback(timerCallback: TimerCallback): Boolean{
        if(callbacks.contains(timerCallback)){
            return false
        }
        return callbacks.add(timerCallback)
    }

    fun unregisterTimerCallback(timerCallback: TimerCallback): Boolean{
        return callbacks.remove(timerCallback)
    }

    interface TimerCallback {
        fun onTimerRemoved(timer:TimerState)
        fun onTimerAdded(timer:TimerState)
    }

}