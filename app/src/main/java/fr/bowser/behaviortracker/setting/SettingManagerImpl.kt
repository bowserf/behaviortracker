package fr.bowser.behaviortracker.setting

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import fr.bowser.behaviortracker.R

class SettingManagerImpl(private val context: Context) : SettingManager,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var timerModificationListener = mutableSetOf<SettingManager.TimerModificationListener>()

    private var timeModification: Int

    private var oneActiveTimerAtATime: Boolean

    private var pomodoroVibrationEnable: Boolean

    private var pomodoroPauseStep: Int

    private var pomodoroStep: Int

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        timeModification = sharedPreferences.getInt(
            context.getString(R.string.pref_key_time_modification),
            context.resources.getInteger(R.integer.settings_default_value_time_modification)
        )

        oneActiveTimerAtATime = sharedPreferences.getBoolean(
            context.getString(R.string.pref_key_one_active_timer),
            false
        )

        pomodoroVibrationEnable = sharedPreferences.getBoolean(
            context.getString(R.string.pref_key_pomodoro_vibration),
            true
        )

        pomodoroPauseStep = sharedPreferences.getInt(
            context.getString(R.string.pref_key_pomodoro_pause_stage),
            context.resources.getInteger(R.integer.settings_default_value_pomodoro_pause_stage)
        )

        pomodoroStep = sharedPreferences.getInt(
            context.getString(R.string.pref_key_pomodoro_stage),
            context.resources.getInteger(R.integer.settings_default_value_pomodoro_stage)
        )
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            context.getString(R.string.pref_key_time_modification) -> {
                timeModification = sharedPreferences.getInt(
                    key,
                    context.resources.getInteger(
                        R.integer.settings_default_value_time_modification
                    )
                )
                timerModificationListener.forEach { it.onTimerModificationChanged(timeModification) }
            }
            context.getString(R.string.pref_key_one_active_timer) -> {
                oneActiveTimerAtATime = sharedPreferences.getBoolean(key, false)
            }
            context.getString(R.string.pref_key_pomodoro_vibration) -> {
                pomodoroVibrationEnable = sharedPreferences.getBoolean(key, true)
            }
            context.getString(R.string.pref_key_pomodoro_pause_stage) -> {
                pomodoroPauseStep = sharedPreferences.getInt(
                    key, context.resources
                        .getInteger(R.integer.settings_default_value_pomodoro_pause_stage)
                )
            }
            context.getString(R.string.pref_key_pomodoro_stage) -> {
                pomodoroStep = sharedPreferences.getInt(
                    key, context.resources
                        .getInteger(R.integer.settings_default_value_pomodoro_stage)
                )
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

    override fun isOneActiveTimerAtATime(): Boolean {
        return oneActiveTimerAtATime
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