package fr.bowser.behaviortracker.setting

import android.content.Context
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import fr.bowser.behaviortracker.R

class TimeModificationSettings(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    private lateinit var slider: SeekBar

    private lateinit var timerModificationText: TextView

    init {
        dialogLayoutResource = R.layout.preference_dialog_time_modification
    }

    override fun onCreateDialogView(): View {
        val view = super.onCreateDialogView()

        timerModificationText = view.findViewById(R.id.tv_time_modification)

        slider = view.findViewById(R.id.slider_time_modification)
        slider.setOnSeekBarChangeListener(CustomSeekBarChangeListener())
        slider.max = MAX_DURATION - MIN_VALUE_TIME

        return view
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val timerModification = getPersistedInt(getDefaultValue()) - MIN_VALUE_TIME

        slider.progress = timerModification
        timerModificationText.text = convertTime(timerModification).toString()
    }

    private fun getDefaultValue():Int{
        val resources = context.resources
        return when(key){
            resources.getString(R.string.pref_key_time_modification) -> {
                resources.getInteger(R.integer.settings_default_value_time_modification)
            }
            resources.getString(R.string.pref_key_pomodoro_stage) -> {
                resources.getInteger(R.integer.settings_default_value_pomodoro_stage)
            }
            resources.getString(R.string.pref_key_pomodoro_pause_stage) -> {
                resources.getInteger(R.integer.settings_default_value_pomodoro_pause_stage)
            }
            else -> { throw IllegalStateException("Unknown preference key : $key") }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)

        if (positiveResult) {
            val value = slider.progress
            persistInt(convertTime(value))
        }
    }

    private fun convertTime(progress: Int): Int {
        return MIN_VALUE_TIME + progress
    }

    private inner class CustomSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            timerModificationText.text = convertTime(progress).toString()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // nothing to do
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // nothing to do
        }
    }

    companion object {
        private const val MAX_DURATION = 60
        private const val MIN_VALUE_TIME = 5
    }
}