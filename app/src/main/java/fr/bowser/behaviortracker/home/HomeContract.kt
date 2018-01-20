package fr.bowser.behaviortracker.home

interface HomeContract {

    interface View{

        fun displayTimerView()

        fun displayTimelineView()

    }

    interface Presenter{

        fun onClickTimer()

        fun onClickTimeline()

    }

}