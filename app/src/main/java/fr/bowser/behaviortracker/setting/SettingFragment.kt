package fr.bowser.behaviortracker.setting

import android.os.Bundle
import android.preference.PreferenceFragment
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject


class SettingFragment : PreferenceFragment() {

    @Inject
    lateinit var presenter: SettingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_list)

        setupGraph()

        presenter.start()
    }

    override fun onDestroy() {
        presenter.stop()
        super.onDestroy()
    }

    private fun setupGraph() {
        val component = DaggerSettingComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(activity!!))
                .settingPresenterModule(SettingPresenterModule())
                .build()
        component.inject(this)
    }

}