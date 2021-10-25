package fr.bowser.behaviortracker.timerlist

import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.feature_string.StringManager

class TimerPresenter(
    private val screen: TimerContract.Screen,
    private val timerListManager: TimerListManager,
    private val timeManager: TimeManager,
    private val stringManager: StringManager,
    private val isInstantApp: Boolean
) : TimerContract.Presenter {

    private val timerListManagerListener = createTimerListManagerListener()

    override fun init() {
        val sections = listOf(
            TimerListSection(
                stringManager.getString(R.string.timer_list_section_active_title),
                true
            ),
            TimerListSection(
                stringManager.getString(R.string.timer_list_section_inactive_title),
                false
            )
        )
        screen.displayTimerListSections(sections)

        updateListVisibility()
    }

    override fun onStart() {
        timerListManager.addListener(timerListManagerListener)
    }

    override fun onStop() {
        timerListManager.removeListener(timerListManagerListener)
    }

    override fun onClickResetAll() {
        screen.displayResetAllDialog()
    }

    override fun onClickResetAllTimers() {
        val timers = timerListManager.getTimerList()
        timers.forEach { timer ->
            timeManager.updateTime(timer, 0f)
            timeManager.stopTimer(timer)
        }
    }

    override fun onClickSettings() {
        screen.displaySettingsView()
    }

    override fun onClickRemoveAllTimers() {
        screen.displayRemoveAllTimersConfirmationDialog()
    }

    override fun onClickConfirmRemoveAllTimers() {
        timerListManager.removeAllTimers()
    }

    override fun onClickAlarm() {
        screen.displayAlarmTimerDialog()
    }

    override fun onClickRewards() {
        screen.displayRewardsView()
    }

    override fun onClickAddTimer() {
        screen.displayCreateTimerView()
    }

    override fun isInstantApp(): Boolean {
        return isInstantApp
    }

    private fun updateListVisibility() {
        val timerList = timerListManager.getTimerList()
        if (timerList.isEmpty()) {
            screen.displayEmptyListView()
        } else {
            screen.displayListView()
        }
    }

    private fun createTimerListManagerListener() = object : TimerListManager.Listener {
        override fun onTimerRemoved(removedTimer: Timer) {
            updateListVisibility()
        }

        override fun onTimerAdded(updatedTimer: Timer) {
            updateListVisibility()
        }

        override fun onTimerRenamed(updatedTimer: Timer) {
            // nothing to do
        }
    }
}