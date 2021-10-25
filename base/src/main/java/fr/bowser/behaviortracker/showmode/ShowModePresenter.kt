package fr.bowser.behaviortracker.showmode

import fr.bowser.behaviortracker.timer.TimerListManager

class ShowModePresenter(
    private val screen: ShowModeContract.Screen,
    private val timerListManager: TimerListManager
) : ShowModeContract.Presenter {

    private var keepScreeOn = false

    override fun onStart(selectedTimerId: Long) {
        val timers = timerListManager.getTimerList()

        var selectedTimerPosition = 0
        for (i in timers.indices) {
            val timer = timers[i]
            if (timer.id == selectedTimerId) {
                selectedTimerPosition = i
            }
        }

        screen.displayTimerList(timers, selectedTimerPosition)
    }

    override fun onClickScreeOn() {
        keepScreeOn = true
        screen.keepScreeOn(true)
    }

    override fun onClickScreeOff() {
        keepScreeOn = false
        screen.keepScreeOn(false)
    }

    override fun keepScreenOn(): Boolean {
        return keepScreeOn
    }
}