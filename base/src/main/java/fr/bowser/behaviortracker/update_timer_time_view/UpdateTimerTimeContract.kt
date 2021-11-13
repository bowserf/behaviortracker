package fr.bowser.behaviortracker.update_timer_time_view

interface UpdateTimerTimeContract {

    interface Presenter {
        fun start()
        fun onClickValidate(hour: Int, minute: Int)
    }

    interface Screen {
        fun displayTimerInformation(currentHour: Int, currentMinute: Int)
    }
}
