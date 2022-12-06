package fr.bowser.behaviortracker.update_timer_time_view

import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_list.TimerListManager

class UpdateTimerTimeViewPresenter(
    private val screen: UpdateTimerTimeViewContract.Screen,
    timerListManager: TimerListManager,
    private val timeManager: TimerManager,
    timerId: Long
) : UpdateTimerTimeViewContract.Presenter {

    private var timer = timerListManager.getTimerList().first { it.id == timerId }

    override fun start() {
        val hour = (timer.time / 3600).toInt()
        val remainingSeconds = timer.time % 3600
        val minute = (remainingSeconds / 60).toInt()
        screen.displayTimerInformation(hour, minute)
    }

    override fun onClickValidate(hour: Int, minute: Int) {
        val newTime = (hour * 3600 + minute * 60).toFloat()
        timeManager.updateTime(timer, newTime)
    }
}
