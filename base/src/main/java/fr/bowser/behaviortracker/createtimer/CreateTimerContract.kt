package fr.bowser.behaviortracker.createtimer

interface CreateTimerContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun onClickCreateTimer(name: String, hour: Int, minute: Int, startNow: Boolean)

        fun onClickColor(oldSelectedPosition: Int, selectedPosition: Int)

        fun enablePomodoroMode(isPomodoro: Boolean)

        fun onClickChangeColorState()

        fun onClickChangeTimeState()
    }

    interface Screen {

        fun exitViewAfterSucceedTimerCreation()

        fun displayNameError()

        fun updateColorList(oldSelectedPosition: Int, selectedPosition: Int)

        fun fillColorList(colorPosition: Int)

        fun updateContainerTimeState(isDisplay: Boolean)

        fun updateContainerColorState(isDisplay: Boolean)
    }
}
