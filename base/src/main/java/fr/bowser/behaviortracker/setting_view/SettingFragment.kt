package fr.bowser.behaviortracker.setting_view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class SettingFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var presenter: SettingContract.Presenter

    private lateinit var sendCommentary: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_list, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()

        val keyTimeModification = resources.getString(R.string.pref_key_time_modification)
        val timeModificator = findPreference<TimeModificationDialogPreference>(keyTimeModification)
        timeModificator!!.setTimeUnit(TIME_MODIFICATION_UNIT)

        val keyPomodoroStage = resources.getString(R.string.pref_key_pomodoro_stage)
        val pomodoroStage = findPreference<TimeModificationDialogPreference>(keyPomodoroStage)
        pomodoroStage!!.setTimeUnit(DURATION_STAGE_UNIT)

        val keyPomodoroPauseStage = resources.getString(R.string.pref_key_pomodoro_pause_stage)
        val pomodoroPauseStage = findPreference<TimeModificationDialogPreference>(
            keyPomodoroPauseStage
        )
        pomodoroPauseStage!!.setTimeUnit(DURATION_STAGE_UNIT)

        val key = getString(fr.bowser.behaviortracker.R.string.pref_key_send_commentary)
        sendCommentary = findPreference(key)!!
        sendCommentary.onPreferenceClickListener = onPreferenceClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeToolbar(view)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    private fun initializeToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupGraph() {
        val component = DaggerSettingComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .settingPresenterModule(SettingPresenterModule())
            .build()
        component.inject(this)
    }

    private fun sendCommentary() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$SUPPORT_EMAIL")
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            resources.getString(fr.bowser.behaviortracker.R.string.settings_send_email_subject)
        )
        try {
            startActivity(
                Intent.createChooser(
                    intent,
                    resources.getString(fr.bowser.behaviortracker.R.string.settings_send_email_choose_app)
                )
            )
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                resources.getString(fr.bowser.behaviortracker.R.string.settings_send_email_no_application_available),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        var dialogFragment: DialogFragment? = null
        if (preference is TimeModificationDialogPreference) {
            dialogFragment = TimeModificationSettings.newInstance(preference.key)
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(requireFragmentManager(), TimeModificationSettings.TAG)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    private val onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
        when (preference) {
            sendCommentary -> {
                sendCommentary()
                return@OnPreferenceClickListener true
            }
        }
        false
    }

    private fun createScreen() = object : SettingContract.Screen {

    }

    companion object {
        const val SUPPORT_EMAIL = "torcheux.frederic@gmail.com"
        const val TIME_MODIFICATION_UNIT = "s"
        const val DURATION_STAGE_UNIT = "min"
    }
}