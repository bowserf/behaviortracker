package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager

class PomodoroPresenter(private val view: PomodoroContract.View,
                        private val timerListManager: TimerListManager,
                        private val timeManager: TimeManager) : PomodoroContract.Presenter {


}