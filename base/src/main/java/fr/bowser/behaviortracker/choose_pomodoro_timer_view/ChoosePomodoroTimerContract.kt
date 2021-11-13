package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import fr.bowser.behaviortracker.timer.Timer

interface ChoosePomodoroTimerContract {

    interface Presenter {
        fun onStart()
        fun onTimerChose(timer: Timer)
    }

    interface Screen {
        fun displayTimerList(timerList: List<Timer>)
    }
}
