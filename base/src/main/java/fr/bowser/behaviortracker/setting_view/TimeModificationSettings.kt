package fr.bowser.behaviortracker.setting_view

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import fr.bowser.behaviortracker.R

class TimeModificationSettings : PreferenceDialogFragmentCompat() {

    private lateinit var slider: SeekBar

    private lateinit var timerModificationText: TextView

    private lateinit var timeModificationPreference: TimeModificationDialogPreference

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        timeModificationPreference = preference as TimeModificationDialogPreference
        val timerModification = timeModificationPreference.getTimerModification() - MIN_VALUE_TIME

        timerModificationText = view.findViewById(R.id.tv_time_modification)
        timerModificationText.text = computeText(timerModification)

        slider = view.findViewById(R.id.slider_time_modification)
        slider.setOnSeekBarChangeListener(createOnSeekBarChangeListener())
        slider.max = MAX_DURATION - MIN_VALUE_TIME
        slider.progress = timerModification
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val value = slider.progress
            timeModificationPreference.setTimerModification(convertTime(value))
        }
    }

    private fun computeText(progress: Int): String {
        val time = convertTime(progress)
        return time.toString() + timeModificationPreference.getTimeUnit()
    }

    private fun convertTime(progress: Int): Int {
        return MIN_VALUE_TIME + progress
    }

    private fun createOnSeekBarChangeListener(): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                timerModificationText.text = computeText(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // nothing to do
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // nothing to do
            }
        }
    }

    companion object {
        private const val MAX_DURATION = 60
        private const val MIN_VALUE_TIME = 5

        const val TAG = "TimeModificationSettingsTAG"

        fun newInstance(key: String): TimeModificationSettings {
            val fragment = TimeModificationSettings()
            val bundle = Bundle()
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }
}

class TimeModificationDialogPreference : DialogPreference {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var timerModification: Int = 0

    private lateinit var timeUnit: String

    override fun getDialogLayoutResource(): Int {
        return R.layout.preference_dialog_time_modification
    }

    override fun onGetDefaultValue(a: TypedArray?, index: Int): Any {
        return a!!.getInt(index, timerModification)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        if (defaultValue != null) {
            setTimerModification(defaultValue as Int)
        } else {
            setTimerModification(getPersistedInt(timerModification))
        }
    }

    fun setTimerModification(timerModification: Int) {
        this.timerModification = timerModification

        persistInt(timerModification)
    }

    fun getTimerModification(): Int {
        return timerModification
    }

    fun setTimeUnit(timeUnit: String) {
        this.timeUnit = timeUnit
    }

    fun getTimeUnit(): String {
        return timeUnit
    }
}
