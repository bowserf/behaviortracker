package fr.bowser.behaviortracker.home

interface TimerContract {

    interface View{

        fun displayCreateTimerView()

    }

    interface Presenter{

        fun onClickAddTimer()

    }

}