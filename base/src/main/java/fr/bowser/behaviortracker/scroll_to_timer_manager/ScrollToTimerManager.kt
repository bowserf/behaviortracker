package fr.bowser.behaviortracker.scroll_to_timer_manager

interface ScrollToTimerManager {

    fun scrollToTimer(timerId: Long)

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {
        fun scrollToTimer(timerId: Long)
    }
}
