package fr.bowser.behaviortracker.timeritem

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.notification.TimeService
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.time.TimeProvider
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerItemModule(private val screen: TimerItemContract.Screen) {

    @GenericScope(component = TimerItemComponent::class)
    @Provides
    fun provideTimerItemPresenter(
        context: Context,
        timeManager: TimeManager,
        timerListManager: TimerListManager,
        settingManager: SettingManager,
        pomodoroManager: PomodoroManager,
        timeProvider: TimeProvider
    ): TimerItemContract.Presenter {
        val addOn = createAddOn(context)
        return TimerItemPresenter(
            screen,
            timeManager,
            timerListManager,
            settingManager,
            pomodoroManager,
            timeProvider,
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