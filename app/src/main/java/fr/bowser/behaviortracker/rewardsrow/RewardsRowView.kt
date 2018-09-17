package fr.bowser.behaviortracker.rewardsrow

import android.app.Activity
import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.inapp.InApp
import javax.inject.Inject

class RewardsRowView(context: Context) : CardView(context), RewardsRowContract.Screen {

    private val icon: ImageView
    private val title: TextView
    private val price: TextView

    private var inApp: InApp? = null

    @Inject
    lateinit var presenter: RewardsRowPresenter

    init {
        setupGraph()

        inflate(context, R.layout.item_rewards, this)

        // card radius
        radius = resources.getDimension(R.dimen.default_space_half)

        setOnClickListener { presenter.onItemClicked(inApp!!.sku) }

        icon = findViewById(R.id.rewards_icon)
        title = findViewById(R.id.rewards_title)
        price = findViewById(R.id.rewards_price)
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
            icon.setImageResource(getIconId())
        }
    }

    @DrawableRes
    private fun getIconId(): Int {
        val feature = inApp?.feature
        return when (feature) {
            InApp.FEATURE_MARSHMALLOW -> R.drawable.ic_marshmallow
            InApp.FEATURE_NOUGAT -> R.drawable.ic_nougat
            InApp.FEATURE_OREO -> R.drawable.ic_oreo
            InApp.FEATURE_PIE -> R.drawable.ic_pie
            else -> throw IllegalStateException("Unkown feature : $feature")
        }
    }

}