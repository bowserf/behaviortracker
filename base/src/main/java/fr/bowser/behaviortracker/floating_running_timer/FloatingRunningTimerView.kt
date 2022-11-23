package fr.bowser.behaviortracker.floating_running_timer

import android.content.Context
import androidx.cardview.widget.CardView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import javax.inject.Inject

class FloatingRunningTimerView(context: Context) : CardView(context) {

    @Inject
    lateinit var presenter: FloatingRunningTimerContract.Presenter

    private val screen = createScreen()

    init {
        setupGraph()

        inflate(context, R.layout.floating_running_timer_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onStart()
    }

    override fun onDetachedFromWindow() {
        presenter.onStop()
        super.onDetachedFromWindow()
    }

    private fun setupGraph() {
        val component = DaggerFloatingRunningTimerComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .floatingRunningTimerModule(FloatingRunningTimerModule(screen))
            .build()
        component.inject(this)
    }

    private fun createScreen() = object : FloatingRunningTimerContract.Screen {
    }
}
