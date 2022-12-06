package fr.bowser.behaviortracker.floating_running_timer_view

interface FloatingRunningTimerViewContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun onClickUpdateTime()

        fun onClickPlayPause()
    }

    interface Screen {

        fun displayUpdateTimer(timerId: Long)

        fun changeState(isPlaying: Boolean)

        fun updateTime(time: String)

        fun displayView(display: Boolean)

        fun updateTimer(timerName: String)
    }
}
