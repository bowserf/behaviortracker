package fr.bowser.behaviortracker.home

interface HomeContract {

    interface View{

        fun displayTimerView()

        fun displayTimelineView()

    }

    interface Presenter{

        fun start()

        fun onClickTimer()

        fun onClickTimeline()

    }

}