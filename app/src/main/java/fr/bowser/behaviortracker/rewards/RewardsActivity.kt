package fr.bowser.behaviortracker.rewards

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.inapp.InApp
import javax.inject.Inject

class RewardsActivity : AppCompatActivity(), RewardsContract.Screen {

    private lateinit var rewardsAdapter: RewardsAdapter

    @Inject
    lateinit var presenter: RewardsPresenter

    private lateinit var list: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        setupGraph()

        initializeToolbar()

        initRewardsList()
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun displayListInApps(inApps: List<InApp>) {
        rewardsAdapter.setInAppList(inApps)
    }

    override fun displaySuccessPurchaseMessage() {
        Snackbar.make(list, getString(R.string.rewards_purchase_success), Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun displayFailPurchaseMessage() {
        Snackbar.make(list, getString(R.string.rewards_purchase_success), Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun initializeToolbar() {
        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar?.let {
            it.title = resources.getString(R.string.activity_rewards_title)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        myToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initRewardsList() {
        list = findViewById(R.id.list_rewards)

        list.layoutManager = LinearLayoutManager(this)
        list.setHasFixedSize(true)
        rewardsAdapter = RewardsAdapter()
        list.adapter = rewardsAdapter

        val margin = resources.getDimensionPixelOffset(R.dimen.default_space_1_5)
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val currentPosition = parent.getChildAdapterPosition(view)
                outRect.top = if (currentPosition == 0) margin else 0
                outRect.left = margin
                outRect.right = margin
                outRect.bottom = margin
            }
        })
    }

    private fun setupGraph() {
        val component = DaggerRewardsComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(this))
            .rewardsPresenterModule(RewardsPresenterModule(this))
            .build()
        component.inject(this)
    }

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, RewardsActivity::class.java)
            context.startActivity(intent)
        }
    }
}