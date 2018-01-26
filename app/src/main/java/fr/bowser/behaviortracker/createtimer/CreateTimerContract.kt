package fr.bowser.behaviortracker.createtimer

interface CreateTimerContract {

    interface View{

        fun exitViewAfterSucceedTimerCreation()

    }

    interface Presenter{

        fun createTimer(name: String, color: Int)

    }

}