package fr.bowser.behaviortracker.pomodoro

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class PomodoroFragment : Fragment(), PomodoroContract.View {

    @Inject
    lateinit var presenter: PomodoroPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupGraph(){
        val builder = DaggerPomodoroComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .pomodoroModule(PomodoroModule(this))
                .build()
        builder.inject(this)
    }

    companion object {
        const val TAG = "PomodoroFragment"
    }

}