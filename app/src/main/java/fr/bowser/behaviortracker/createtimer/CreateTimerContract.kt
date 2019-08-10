package fr.bowser.behaviortracker.createtimer

interface CreateTimerContract {

    interface View {

        fun exitViewAfterSucceedTimerCreation()

        fun displayNameError()

        fun updateColorList(oldSelectedPosition: Int, selectedPosition: Int)
    }

    interface Presenter {

        fun createTimer(name: String, startNow: Boolean)

        fun changeSelectedColor(oldSelectedPosition: Int, selectedPosition: Int)

        fun enablePomodoroMode(isPomodoro: Boolean)
    }
}