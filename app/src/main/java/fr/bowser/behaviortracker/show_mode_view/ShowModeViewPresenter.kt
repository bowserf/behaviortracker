package fr.bowser.behaviortracker.show_mode_view

import fr.bowser.behaviortracker.timer_repository.TimerRepository

class ShowModeViewPresenter(
    private val screen: ShowModeViewContract.Screen,
    private val timerRepository: TimerRepository,
) : ShowModeViewContract.Presenter {

    private var keepScreeOn = false

    override fun onStart(selectedTimerId: Long) {
        val timers = timerRepository.getTimerList()

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
