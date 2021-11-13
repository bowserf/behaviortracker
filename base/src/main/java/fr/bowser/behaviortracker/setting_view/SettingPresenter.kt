package fr.bowser.behaviortracker.setting_view

import android.content.SharedPreferences
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.feature_string.StringManager

class SettingPresenter(
    private val sharedPreferences: SharedPreferences,
    private val stringManager: StringManager,
    private val eventManager: EventManager
) : SettingContract.Presenter {

    override fun onStart() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(
            onSharedPreferenceChangerListener
        )
    }

    override fun onStop() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(
            onSharedPreferenceChangerListener
        )
    }

    private val onSharedPreferenceChangerListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                stringManager.getString(R.string.pref_key_time_modification) -> {
                    eventManager.sendNewTimeFixTimerDurationEvent(sharedPreferences.getInt(key, -1))
                }
            }
        }
}
