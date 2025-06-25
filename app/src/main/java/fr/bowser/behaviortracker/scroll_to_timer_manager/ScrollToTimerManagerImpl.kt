package fr.bowser.behaviortracker.scroll_to_timer_manager

class ScrollToTimerManagerImpl : ScrollToTimerManager {

    private val listeners = mutableListOf<ScrollToTimerManager.Listener>()

    override fun scrollToTimer(timerId: Long) {
        listeners.forEach {
            it.scrollToTimer(timerId)
        }
    }

    override fun addListener(listener: ScrollToTimerManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun removeListener(listener: ScrollToTimerManager.Listener) {
        listeners.remove(listener)
    }
}
