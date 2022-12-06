package fr.bowser.behaviortracker.timer_service

interface TimeServiceContract {

    interface Screen {
        fun renameTimerNotification(name: String)
        fun dismissNotification()
        fun updateTimeNotification(time: String)
        fun displayTimerNotification(title: String, message: String)
        fun resumeTimerNotification(title: String)
        fun pauseTimerNotification()
        fun continuePomodoroNotification()
    }

    interface Presenter {
        fun attach()
        fun detach()
        fun start()
        fun dismissNotification()
    }
}
