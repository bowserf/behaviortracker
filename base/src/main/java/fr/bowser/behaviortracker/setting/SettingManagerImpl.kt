package fr.bowser.behaviortracker.setting

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import fr.bowser.behaviortracker.R
import fr.bowser.feature_string.StringManager

class SettingManagerImpl(
    private val context: Context,
    private val stringManager: StringManager
) : SettingManager {

    private val timerModificationListener = mutableSetOf<SettingManager.TimerModificationListener>()

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val sharedPreferenceChangeListener = createSharedPreferenceChangeListener()

    private var timeModification: Int

    private var pomodoroVibrationEnable: Boolean

    private var pomodoroPauseStep: Int

    private var pomodoroStep: Int

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)

        timeModification = sharedPreferences.getInt(
            stringManager.getString(R.string.pref_key_time_modification),
            context.resources.getInteger(R.integer.settings_default_value_time_modification_seconds)
        )

        pomodoroVibrationEnable = sharedPreferences.getBoolean(
            stringManager.getString(R.string.pref_key_pomodoro_vibration),
            true
        )

        pomodoroPauseStep = sharedPreferences.getInt(
            stringManager.getString(R.string.pref_key_pomodoro_pause_stage),
            context.resources.getInteger(R.integer.settings_default_value_pomodoro_pause_stage_minutes)
        ) * 60

        pomodoroStep = sharedPreferences.getInt(
            stringManager.getString(R.string.pref_key_pomodoro_stage),
            context.resources.getInteger(R.integer.settings_default_value_pomodoro_stage_minutes)
        ) * 60
    }

    private fun createSharedPreferenceChangeListener() =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                stringManager.getString(R.string.pref_key_time_modification) -> {
                    timeModification = sharedPreferences.getInt(
                        key,
                        context.resources.getInteger(
                            R.integer.settings_default_value_time_modification_seconds
                        )
                    )
                    timerModificationListener.forEach {
                        it.onTimerModificationChanged(
                            timeModification
                        )
                    }
                }
                stringManager.getString(R.string.pref_key_pomodoro_vibration) -> {
                    pomodoroVibrationEnable = sharedPreferences.getBoolean(key, true)
                }
                stringManager.getString(R.string.pref_key_pomodoro_pause_stage) -> {
                    pomodoroPauseStep = sharedPreferences.getInt(
                        key,
                        context.resources.getInteger(
                            R.integer.settings_default_value_pomodoro_pause_stage_minutes
                        )
                    ) * 60
                }
                stringManager.getString(R.string.pref_key_pomodoro_stage) -> {
                    pomodoroStep = sharedPreferences.getInt(
                        key,
                        context.resources.getInteger(
                            R.integer.settings_default_value_pomodoro_stage_minutes
                        )
                    ) * 60
                }
            }
        }

    override fun registerTimerModificationListener(listener: SettingManager.TimerModificationListener) {
        timerModificationListener.add(listener)
    }

    override fun unregisterTimerModificationListener(listener: SettingManager.TimerModificationListener) {
        timerModificationListener.remove(listener)
    }

    override fun getTimerModification(): Int {
        return timeModification
    }

    override fun isPomodoroVibrationEnable(): Boolean {
        return pomodoroVibrationEnable
    }

    override fun getPomodoroPauseStepDuration(): Long {
        return pomodoroPauseStep.toLong()
    }

    override fun getPomodoroStepDuration(): Long {
        return pomodoroStep.toLong()
    }
}