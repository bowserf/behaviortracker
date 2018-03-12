package fr.bowser.behaviortracker.setting

import android.os.Bundle
import android.preference.PreferenceFragment
import fr.bowser.behaviortracker.R


class SettingFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_list)
    }

}