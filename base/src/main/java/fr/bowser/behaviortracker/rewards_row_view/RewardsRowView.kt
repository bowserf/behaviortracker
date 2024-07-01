package fr.bowser.behaviortracker.rewards_row_view

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.inapp.InApp
import fr.bowser.behaviortracker.utils.ViewExtension.bind
import javax.inject.Inject

class RewardsRowView(context: Context) : CardView(context) {

    @Inject
    lateinit var presenter: RewardsRowViewContract.Presenter

    private val screen = createScreen()

    private val title: TextView by bind(R.id.rewards_row_view_title)
    private val price: TextView by bind(R.id.rewards_row_view_price)
    private val background: ImageView by bind(R.id.rewards_row_view_background)

    private var inApp: InApp? = null

    init {
        setupGraph()

        inflate(context, R.layout.rewards_row_view, this)

        // card radius
        radius = resources.getDimension(R.dimen.default_space_half)

        setOnClickListener { presenter.onItemClicked(inApp!!.sku) }
    }

    fun setInApp(inApp: InApp) {
        this@RewardsRowView.inApp = inApp
        updateUI()
    }

    private fun setupGraph() {
        val component = DaggerRewardsRowViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .rewardsRowViewModule(RewardsRowViewModule(screen))
            .build()
        component.inject(this)
    }

    private fun updateUI() {
        inApp?.let {
            title.text = it.description
            price.text = it.price
            background.setImageDrawable(ContextCompat.getDrawable(context, getDrawableId()))
        }
    }

    @DrawableRes
    private fun getDrawableId(): Int {
        return when (val feature = inApp?.feature) {
            InApp.FEATURE_MARSHMALLOW -> R.drawable.marshmallow
            InApp.FEATURE_NOUGAT -> R.drawable.nougat
            InApp.FEATURE_OREO -> R.drawable.oreo
            InApp.FEATURE_PIE -> R.drawable.pie
            else -> throw IllegalStateException("Unknown feature : $feature")
        }
    }

    private fun createScreen() = object : RewardsRowViewContract.Screen {
        override fun getActivity(): Activity {
            if (context !is Activity) {
                throw IllegalStateException("Context should be a context Activity")
            }
            return context as Activity
        }

        override fun displayStoreConnectionError() {
            MaterialAlertDialogBuilder(context)
                .setMessage(R.string.rewards_purchase_fail)
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }
}
