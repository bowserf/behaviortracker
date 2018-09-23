package fr.bowser.behaviortracker.choosepomodorotimer

import fr.bowser.behaviortracker.pomodoro.PomodoroConstants.POMODORO_DURATION
import fr.bowser.behaviortracker.pomodoro.PomodoroConstants.REST_DURATION
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class ChoosePomodoroTimerPresenter(private val timerListManager: TimerListManager,
                                   private val pomodoroManager: PomodoroManager,
                                   private val pauseTimer: Timer) {

    fun getTimerList(): List<Timer> {
        return timerListManager.getTimerList()
    }

    fun startPomodoro(timer: Timer) {
        pomodoroManager.startPomodoro(
                timer,
                POMODORO_DURATION,
                pauseTimer,
                REST_DURATION)
    }

}