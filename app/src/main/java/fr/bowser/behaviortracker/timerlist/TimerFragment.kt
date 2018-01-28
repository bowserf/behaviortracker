package fr.bowser.behaviortracker.timerlist

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerActivity
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerState
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

        view.findViewById<View>(R.id.button_add_timer).setOnClickListener { presenter.onClickAddTimer() }
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun displayCreateTimerView() {
        CreateTimerActivity.startActivity(context)
    }

    override fun displayTimerList(timers: ArrayList<TimerState>) {
        timerAdapter.setTimersList(timers)
    }

    override fun onTimerRemoved(timer: Timer) {
        val timerState = timerAdapter.getTimerStateFromTimer(timer)
        timerAdapter.removeTimer(timerState)
    }

    override fun onTimerAdded(timer: Timer) {
        timerAdapter.notifyDataSetChanged()
    }

    private fun setupGraph() {
        val build = DaggerTimerComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .timerModule(TimerModule(this))
                .build()
        build.inject(this)
    }

    private fun initializeList(view: View) {
        val list = view.findViewById<RecyclerView>(R.id.list_timers)
        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list.setHasFixedSize(true)
        timerAdapter = TimerAdapter()
        list.adapter = timerAdapter

        val spaceBetweenItems = resources.getDimensionPixelOffset(R.dimen.margin_between_timer_items)
        val margin = resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                val currentPosition = parent?.getChildLayoutPosition(view)
                if (parent?.adapter?.itemCount != currentPosition) {
                    outRect?.bottom = spaceBetweenItems
                    outRect?.left = margin
                    outRect?.right = margin
                }
                if (currentPosition == 0) {
                    outRect?.top = margin
                }
            }
        })
    }

    companion object {
        const val TAG = "TimerFragment"
    }

}