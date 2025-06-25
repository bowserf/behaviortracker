package fr.bowser.behaviortracker.show_mode_view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.FragmentExtension.bind
import fr.bowser.behaviortracker.utils.applyStatusBarPadding
import javax.inject.Inject

class ShowModeViewFragment : Fragment(R.layout.show_mode_view_fragment) {

    @Inject
    lateinit var presenter: ShowModeViewContract.Presenter

    private val screen = createScreen()

    private val viewPager: ViewPager2 by bind(R.id.show_mode_view_pager)

    private val adapter: ShowModeViewAdapter = ShowModeViewAdapter()

    private val args: ShowModeViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager.adapter = adapter

        val toolbar = view.findViewById<Toolbar>(R.id.show_mode_view_toolbar)!!
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.title = ""
        toolbar.applyStatusBarPadding()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart(args.selectedTimerId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_mode_view_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuScreenOff = menu.findItem(R.id.show_mode_view_menu_keep_screen_off)
        val menuScreenOn = menu.findItem(R.id.show_mode_view_menu_keep_screen_on)

        if (presenter.keepScreenOn()) {
            menuScreenOff?.isVisible = true
            menuScreenOn?.isVisible = false
        } else {
            menuScreenOff?.isVisible = false
            menuScreenOn?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_mode_view_menu_keep_screen_on -> {
                presenter.onClickScreeOn()
                return true
            }

            R.id.show_mode_view_menu_keep_screen_off -> {
                presenter.onClickScreeOff()
                return true
            }
        }
        return false
    }

    private fun setupGraph() {
        val build = DaggerShowModeViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .showModeViewModule(ShowModeViewModule(screen))
            .build()
        build.inject(this)
    }

    private fun createScreen() = object : ShowModeViewContract.Screen {

        override fun displayTimerList(timers: List<Timer>, selectedTimerPosition: Int) {
            adapter.setData(timers)
            viewPager.post { viewPager.setCurrentItem(selectedTimerPosition, false) }
        }

        override fun keepScreeOn(keepScreenOn: Boolean) {
            if (keepScreenOn) {
                requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            requireActivity().invalidateOptionsMenu()
        }
    }
}
