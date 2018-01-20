package fr.bowser.behaviortracker.timer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.home.DaggerTimerComponent
import fr.bowser.behaviortracker.home.TimerContract
import fr.bowser.behaviortracker.home.TimerModule
import javax.inject.Inject

class TimerFragment : Fragment(), TimerContract.View {

    @Inject
    lateinit var presenter: TimerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, null)
    }

    private fun setupGraph() {
        val build = DaggerTimerComponent.builder()
                .timerModule(TimerModule(this))
                .build()
        build.inject(this)
    }

    companion object {
        const val TAG = "TimerFragment"
    }

}