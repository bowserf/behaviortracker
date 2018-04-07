package fr.bowser.behaviortracker.setting

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import fr.bowser.behaviortracker.R

class SettingManagerImpl(private val context: Context) : SettingManager,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private var timeModification = 0

    private var oneActiveTimerAtATime = false

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        timeModification = sharedPreferences.getInt(
                context.getString(R.string.pref_key_time_modification),
                context.resources.getInteger(R.integer.settings_default_value_time_modification))

        oneActiveTimerAtATime = sharedPreferences.getBoolean(
                context.getString(R.string.pref_key_one_active_timer),
                false)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            context.getString(R.string.pref_key_time_modification) -> {
                timeModification = sharedPreferences.getInt(
                        key,
                        context.resources.getInteger(R.integer.settings_default_value_time_modification))
            }
            context.getString(R.string.pref_key_one_active_timer) -> {
                oneActiveTimerAtATime = sharedPreferences.getBoolean(key, false
                )
            }
        }
    }

    override fun getTimerModification(): Int {
        return timeModification
    }

    override fun isOneActiveTimerAtATime(): Boolean {
        return oneActiveTimerAtATime
    }
}