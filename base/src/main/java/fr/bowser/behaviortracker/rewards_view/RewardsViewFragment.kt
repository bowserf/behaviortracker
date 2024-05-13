package fr.bowser.behaviortracker.rewards_view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.inapp.InApp
import fr.bowser.behaviortracker.utils.applyStatusBarPadding
import javax.inject.Inject

class RewardsViewFragment : Fragment(R.layout.rewards_view_fragment) {

    @Inject
    lateinit var presenter: RewardsViewContract.Presenter

    private val screen = createScreen()

    private lateinit var rewardsAdapter: RewardsViewAdapter

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

    private fun createScreen() = object : RewardsViewContract.Screen {
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
        val toolbar = view.findViewById<Toolbar>(R.id.timer_list_view_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.applyStatusBarPadding()
    }

    private fun initRewardsList(view: View) {
        list = view.findViewById(R.id.list_rewards)

        list.layoutManager = LinearLayoutManager(requireContext())
        list.setHasFixedSize(true)
        rewardsAdapter = RewardsViewAdapter()
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

        ViewCompat.setOnApplyWindowInsetsListener(list) { v, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                rightMargin = insets.right
            }
            v.updatePadding(bottom = insets.bottom)

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupGraph() {
        val component = DaggerRewardsViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .rewardsViewModule(RewardsViewModule(screen))
            .build()
        component.inject(this)
    }
}
