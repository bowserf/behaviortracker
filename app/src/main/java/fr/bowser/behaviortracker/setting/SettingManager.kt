package fr.bowser.behaviortracker.setting

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import fr.bowser.behaviortracker.R

class SettingManager(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {

    var timeModification = 0

    private val defaultSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    init {
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this)

        timeModification = defaultSharedPreferences.getInt(
                context.getString(R.string.pref_key_time_modification),
                context.resources.getInteger(R.integer.settings_default_value_time_modification))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            context.getString(R.string.pref_key_time_modification) -> {
                timeModification = sharedPreferences.getInt(
                        key,
                        context.resources.getInteger(R.integer.settings_default_value_time_modification))
            }
        }
    }

}