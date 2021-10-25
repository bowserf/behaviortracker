package fr.bowser.behaviortracker.choosepomodorotimer

import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class ChoosePomodoroTimerPresenter(
    private val screen: ChoosePomodoroTimerContract.Screen,
    private val timerListManager: TimerListManager,
    private val pomodoroManager: PomodoroManager
): ChoosePomodoroTimerContract.Presenter {

    override fun onStart() {
        val timerList = timerListManager.getTimerList()
        screen.displayTimerList(timerList)
    }

    override fun onTimerChose(timer: Timer) {
        pomodoroManager.startPomodoro(timer)
    }
}