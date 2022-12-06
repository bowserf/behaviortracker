package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import fr.bowser.behaviortracker.timer.Timer

interface ChoosePomodoroTimerViewContract {

    interface Presenter {
        fun onStart()
        fun onStop()
        fun onTimerChose(timer: Timer)
    }

    interface Screen {
        fun displayTimerList(timerList: List<Timer>)
    }
}
