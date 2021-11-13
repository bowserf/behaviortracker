package fr.bowser.behaviortracker.rewards_view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.inapp.InApp
import javax.inject.Inject

class RewardsFragment : Fragment(R.layout.fragment_rewards) {

    @Inject
    lateinit var presenter: RewardsContract.Presenter

    private val screen = createScreen()

    private lateinit var rewardsAdapter: RewardsAdapter

    private lateinit var list: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeToolbar(view)

        initRewardsList(view)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    private fun createScreen() = object : RewardsContract.Screen {
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
    }

    private fun initializeToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRewardsList(view: View) {
        list = view.findViewById(R.id.list_rewards)

        list.layoutManager = LinearLayoutManager(requireContext())
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
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .rewardsPresenterModule(RewardsPresenterModule(screen))
            .build()
        component.inject(this)
    }
}
