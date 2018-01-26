package fr.bowser.behaviortracker.timerlist

interface TimerContract {

    interface View{

        fun displayCreateTimerView()

    }

    interface Presenter{

        fun onClickAddTimer()

    }

}