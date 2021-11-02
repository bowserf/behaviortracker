package fr.bowser.behaviortracker.createtimer

interface CreateTimerContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun createTimer(name: String, startNow: Boolean)

        fun changeSelectedColor(oldSelectedPosition: Int, selectedPosition: Int)

        fun enablePomodoroMode(isPomodoro: Boolean)
    }

    interface Screen {

        fun exitViewAfterSucceedTimerCreation()

        fun displayNameError()

        fun updateColorList(oldSelectedPosition: Int, selectedPosition: Int)

        fun fillColorList(colorPosition: Int)
    }
}