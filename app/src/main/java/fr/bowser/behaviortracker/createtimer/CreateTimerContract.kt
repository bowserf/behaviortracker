package fr.bowser.behaviortracker.createtimer

interface CreateTimerContract {

    interface View{

        fun exitViewAfterSucceedTimerCreation()

        fun displayNameError()

    }

    interface Presenter{

        fun createTimer(name: String, color: Int, startNow : Boolean)

    }

}