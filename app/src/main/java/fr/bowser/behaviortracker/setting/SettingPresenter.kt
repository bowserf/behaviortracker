package fr.bowser.behaviortracker.setting

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.event.EventManager

class SettingPresenter(private val context:Context,
                       private val eventManager: EventManager) {

    private val sharedPreferences: SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    fun start(){
        sharedPreferences.registerOnSharedPreferenceChangeListener(
                onSharedPreferenceChangerListener)
    }

    fun stop(){
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(
                onSharedPreferenceChangerListener)
    }

    private val onSharedPreferenceChangerListener
            = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            context.getString(R.string.pref_key_time_modification) -> {
                eventManager.sendNewTimeFixTimerDuration(sharedPreferences.getInt(key, -1))
            }
            context.getString(R.string.pref_key_one_active_timer) -> {
                eventManager.sendExclusiveTimerMode(sharedPreferences.getBoolean(key, false))
            }
        }
    }

}