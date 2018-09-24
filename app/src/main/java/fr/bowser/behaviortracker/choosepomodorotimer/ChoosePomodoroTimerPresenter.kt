package fr.bowser.behaviortracker.choosepomodorotimer

import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class ChoosePomodoroTimerPresenter(private val timerListManager: TimerListManager,
                                   private val pomodoroManager: PomodoroManager) {

    fun getTimerList(): List<Timer> {
        return timerListManager.getTimerList()
    }

    fun startPomodoro(timer: Timer) {
        pomodoroManager.startPomodoro(timer)
    }

}