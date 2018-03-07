package fr.bowser.behaviortracker.timerlist

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.Keep
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.createtimer.CreateTimerDialog
import fr.bowser.behaviortracker.timer.TimerState
import javax.inject.Inject


class TimerFragment : Fragment(), TimerContract.View {

    @Inject
    lateinit var presenter: TimerPresenter

    private lateinit var timerAdapter: TimerAdapter

    private lateinit var fab: FloatingActionButton

    private lateinit var emptyListView: ImageView

    private lateinit var emptyListText: TextView

    private lateinit var list: RecyclerView

    private var mSpanCount: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()

        mSpanCount = resources.getInteger(R.integer.list_timers_number_spans)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeList(view)

        fab = view.findViewById(R.id.button_add_timer)
        fab.setOnClickListener { presenter.onClickAddTimer() }

        emptyListView = view.findViewById(R.id.empty_list_view)
        emptyListText = view.findViewById(R.id.empty_list_text)
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
        CreateTimerDialog.showDialog(activity as AppCompatActivity, true)
    }

    override fun displayTimerList(timers: ArrayList<TimerState>) {
        timerAdapter.setTimersList(timers)
    }

    override fun onTimerRemoved(timer: TimerState, position: Int) {
        timerAdapter.removeTimer(timer, position)
    }

    override fun onTimerAdded(timer: TimerState, position: Int) {
        timerAdapter.addTimer(timer, position)
    }

    override fun displayEmptyListView() {
        list.visibility = GONE
        emptyListView.visibility = View.VISIBLE
        emptyListText.visibility = View.VISIBLE

        val fabAnimator = ObjectAnimator.ofFloat(this, PROPERTY_FAB_ANIMATION, 1f, 1.15f, 1f)
        fabAnimator.duration = FAB_ANIMATION_DURATION
        fabAnimator.repeatCount = 1
        fabAnimator.interpolator = AccelerateDecelerateInterpolator()
        fabAnimator.startDelay = FAB_ANIMATION_DELAY
        fabAnimator.start()
    }

    override fun displayListView() {
        list.visibility = View.VISIBLE
        emptyListView.visibility = View.GONE
        emptyListText.visibility = View.GONE
    }

    private fun setupGraph() {
        val build = DaggerTimerComponent.builder()
                .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context!!))
                .timerModule(TimerModule(this))
                .build()
        build.inject(this)
    }

    private fun initializeList(view: View) {
        list = view.findViewById(R.id.list_timers)

        list.layoutManager = GridLayoutManager(activity, mSpanCount, GridLayoutManager.VERTICAL, false)
        list.setHasFixedSize(true)
        timerAdapter = TimerAdapter()
        list.adapter = timerAdapter

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        val margin = resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                var currentPosition = parent?.getChildAdapterPosition(view)
                // When an item is removed, getChildAdapterPosition returns NO_POSITION but this
                // method is call at the animation start so position = -1 and we don't apply the
                // good top margin. By calling getChildLayoutPosition, we get the view position
                // and we fix the temporary animation issue.
                if (currentPosition == NO_POSITION) {
                    currentPosition = parent?.getChildLayoutPosition(view)
                }
                if (currentPosition != null && currentPosition < mSpanCount) {
                    outRect?.top = margin
                }

                outRect?.left = margin
                outRect?.right = margin
                outRect?.bottom = margin
            }
        })
    }

    /**
     * Setter used by {@link #fabAnimator}
     */
    @Keep
    @SuppressWarnings("unused")
    private fun setFabScale(scale: Float) {
        fab.scaleX = scale
        fab.scaleY = scale
    }

    companion object {
        const val TAG = "TimerFragment"

        private const val PROPERTY_FAB_ANIMATION = "fabScale"

        private const val FAB_ANIMATION_DURATION = 1000L
        private const val FAB_ANIMATION_DELAY = 400L
    }

}