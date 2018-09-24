package fr.bowser.behaviortracker.choosepomodorotimer

import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager

class ChoosePomodoroTimerPresenter(private val timerListManager: TimerListManager,
                                   private val pomodoroManager: PomodoroManager,
                                   private val pomodoroStepDuration: Long,
                                   private val pomodoroPauseStepDuration: Long,
                                   private val pauseTimer: Timer) {

    fun getTimerList(): List<Timer> {
        return timerListManager.getTimerList()
    }

    fun startPomodoro(timer: Timer) {
        pomodoroManager.startPomodoro(
                timer,
                pomodoroStepDuration,
                pauseTimer,
                pomodoroPauseStepDuration)
    }

}