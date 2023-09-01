package fr.bowser.behaviortracker.pomodoro_choose_timer_view

import fr.bowser.behaviortracker.timer.Timer

interface PomodoroChooseTimerViewContract {

    interface Presenter {
        fun onStart()
        fun onStop()
        fun onTimerChose(timer: Timer)
    }

    interface Screen {
        fun displayTimerList(timerList: List<Timer>)
    }
}
