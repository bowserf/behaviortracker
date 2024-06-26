package fr.bowser.behaviortracker.create_timer_view

import android.content.Intent
import androidx.fragment.app.Fragment

interface CreateTimerViewContract {

    interface Presenter {

        fun onStart()

        fun onStop()

        fun onClickCreateTimer(name: String, hour: Int, minute: Int, startNow: Boolean)

        fun onClickColor(oldSelectedPosition: Int, selectedPosition: Int)

        fun enablePomodoroMode(isPomodoro: Boolean)

        fun onClickChangeColorState()

        fun onClickChangeTimeState()

        fun onClickMic(fragment: Fragment)

        fun onSpeechToTextDataRetrieved(data: Intent?)
    }

    interface Screen {

        fun exitViewAfterSucceedTimerCreation()

        fun displayNameError()

        fun updateColorList(oldSelectedPosition: Int, selectedPosition: Int)

        fun fillColorList(colorPosition: Int)

        fun updateContainerTimeState(isDisplay: Boolean)

        fun updateContainerColorState(isDisplay: Boolean)

        fun setTimerName(text: String)

        fun showSpeechIcon(isVisible: Boolean)

        fun showSpeechToTextError()
    }
}
