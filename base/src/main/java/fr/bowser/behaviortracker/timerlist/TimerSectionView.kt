package fr.bowser.behaviortracker.timerlist

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import javax.inject.Inject

class TimerSectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var presenter: TimerSectionContract.Presenter

    private val screen = createScreen()

    private val titleTv by lazy { findViewById<TextView>(R.id.timer_list_section_title) }
    private val timerList by lazy { findViewById<RecyclerView>(R.id.timer_list_section_timers) }
    private val timerAdapter = TimerAdapter()

    init {
        inflate(context, R.layout.timer_list_section, this)

        setupGraph()

        timerList.layoutManager = LinearLayoutManager(context)
        timerList.adapter = timerAdapter

        val swipeHandler = TimerListGesture(context, TimerListGestureListener())
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(timerList)

        val margin = resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
        timerList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                var currentPosition = parent.getChildAdapterPosition(view)
                // When an item is removed, getChildAdapterPosition returns NO_POSITION but this
                // method is call at the animation start so position = -1 and we don't apply the
                // good top margin. By calling getChildLayoutPosition, we get the view position
                // and we fix the temporary animation issue.
                if (currentPosition == RecyclerView.NO_POSITION) {
                    currentPosition = parent.getChildLayoutPosition(view)
                }
                if (currentPosition < 1) {
                    outRect.top = margin
                }

                outRect.bottom = margin
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onResume()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onPause()
    }

    fun populate(section: TimerListSection) {
        presenter.populate(section.title, section.isActive)
    }

    private fun createScreen() = object : TimerSectionContract.Screen {
        override fun updateTitle(title: String) {
            titleTv.text = title
        }

        override fun updateTimerList(timers: List<Timer>) {
            timerAdapter.setTimersList(timers)
        }

        override fun displayTitle(display: Boolean) {
            titleTv.visibility = if (display) View.VISIBLE else View.GONE
        }

        override fun displayCancelDeletionView(cancelDuration: Int) {
            Snackbar.make(
                timerList,
                resources.getString(R.string.timer_view_timer_has_been_removed),
                cancelDuration
            )
                .setAction(android.R.string.cancel) {
                    presenter.onClickCancelTimerDeletion()
                }
                .show()
        }
    }

    inner class TimerListGestureListener : TimerListGesture.Listener {
        override fun onItemMove(fromPosition: Int, toPosition: Int) {
            // nothing to do
        }

        override fun onSelectedChangedUp() {
            // nothing to do
        }

        override fun onSwiped(position: Int) {
            presenter.onTimerSwiped(position)
        }
    }

    private fun setupGraph() {
        val component = DaggerTimerSectionComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .timerSectionModule(TimerSectionModule(screen))
            .build()
        component.inject(this)
    }
}