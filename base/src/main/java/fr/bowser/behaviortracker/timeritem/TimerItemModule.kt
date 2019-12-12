package fr.bowser.behaviortracker.timeritem

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.notification.TimeService
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerItemModule(private val view: TimerItemContract.View) {

    @GenericScope(component = TimerItemComponent::class)
    @Provides
    fun provideTimerItemPresenter(
        context: Context,
        timeManager: TimeManager,
        timerListManager: TimerListManager,
        settingManager: SettingManager,
        pomodoroManager: PomodoroManager
    ): TimerItemPresenter {
        val addOn = createAddOn(context)
        return TimerItemPresenter(
            view,
            timeManager,
            timerListManager,
            settingManager,
            pomodoroManager,
            addOn
        )
    }

    private fun createAddOn(context: Context): TimerItemPresenter.AddOn {
        return object : TimerItemPresenter.AddOn {
            override fun startTimer(timer: Timer) {
                TimeService.startTimer(context, timer.id)
            }

            override fun stopTimer(timer: Timer) {
                TimeService.stopTimer(context, timer.id)
            }
        }
    }
}