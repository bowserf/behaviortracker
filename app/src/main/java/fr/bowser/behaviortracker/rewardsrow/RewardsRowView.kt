package fr.bowser.behaviortracker.rewardsrow

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.inapp.InApp
import javax.inject.Inject

class RewardsRowView(context: Context) : CardView(context), RewardsRowContract.Screen {

    private val title: TextView
    private val price: TextView
    private val background: ImageView

    private var inApp: InApp? = null

    @Inject
    lateinit var presenter: RewardsRowPresenter

    init {
        setupGraph()

        inflate(context, R.layout.item_rewards, this)

        // card radius
        radius = resources.getDimension(R.dimen.default_space_half)

        setOnClickListener { presenter.onItemClicked(inApp!!.sku) }

        title = findViewById(R.id.rewards_title)
        price = findViewById(R.id.rewards_price)
        background = findViewById(R.id.rewards_background)
    }

    override fun setInApp(inApp: InApp) {
        this.inApp = inApp
        updateUI()
    }

    override fun getActivity(): Activity {
        if (context !is Activity) {
            throw IllegalStateException("Context should be a context Activity")
        }
        return context as Activity
    }

    private fun setupGraph() {
        val component = DaggerRewardsRowComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(context))
            .rewardsRowModule(RewardsRowModule(this))
            .build()
        component.inject(this)
    }

    private fun updateUI() {
        inApp?.let {
            title.text = it.description
            price.text = it.price
            background.setImageDrawable(context.getDrawable(getDrawableId()))
        }
    }

    @DrawableRes
    private fun getDrawableId(): Int {
        val feature = inApp?.feature
        return when (feature) {
            InApp.FEATURE_MARSHMALLOW -> R.drawable.marshmallow
            InApp.FEATURE_NOUGAT -> R.drawable.nougat
            InApp.FEATURE_OREO -> R.drawable.oreo
            InApp.FEATURE_PIE -> R.drawable.pie
            else -> throw IllegalStateException("Unkown feature : $feature")
        }
    }
}