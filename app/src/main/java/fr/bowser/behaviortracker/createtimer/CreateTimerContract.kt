package fr.bowser.behaviortracker.createtimer

interface CreateTimerContract {

    interface View{

        fun exitViewAfterSucceedTimerCreation()

        fun displayNameError()

        fun updateColorList(oldSelectedPosition : Int, selectedPosition : Int)

    }

    interface Presenter{

        fun createTimer(name: String, color: Int, startNow : Boolean)

        fun changeSelectedColor(color: Int, oldSelectedPosition : Int, selectedPosition : Int)

    }

}