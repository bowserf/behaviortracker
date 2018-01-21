package fr.bowser.behaviortracker.timer

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.home.DaggerTimerComponent
import fr.bowser.behaviortracker.home.TimerContract
import fr.bowser.behaviortracker.home.TimerModule
import fr.bowser.behaviortracker.model.Timer
import javax.inject.Inject

class TimerFragment : Fragment(), TimerContract.View {

    @Inject
    lateinit var presenter: TimerPresenter

    private lateinit var timerAdapter: TimerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeList(view)

        view.findViewById<View>(R.id.button_add_timer).setOnClickListener{ presenter.onClickAddTimer() }
    }

    override fun displayCreateTimerView() {

    }

    private fun setupGraph() {
        val build = DaggerTimerComponent.builder()
                .timerModule(TimerModule(this))
                .build()
        build.inject(this)
    }

    private fun initializeList(view: View) {
        val list = view.findViewById<RecyclerView>(R.id.list_timers)
        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list.setHasFixedSize(true)
        timerAdapter = TimerAdapter(presenter)
        list.adapter = timerAdapter

        val timerList:MutableList<Timer> = ArrayList()
        timerList.add(Timer(1, 6000, "Work", Color.RED))
        timerList.add(Timer(2, 12345, "Break", Color.BLUE))
        timerAdapter.setTimersList(timerList)
    }

    companion object {
        const val TAG = "TimerFragment"
    }

}