package fr.bowser.behaviortracker.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject


class PomodoroFragment : Fragment(), PomodoroContract.Screen {

    @Inject
    lateinit var presenter: PomodoroPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pomodoro, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    private fun setupGraph() {
        val build = DaggerPomodoroComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .pomodoroModule(PomodoroModule(this))
                .build()
        build.inject(this)
    }

    companion object {
        const val TAG = "PomodoroFragment"
    }

}