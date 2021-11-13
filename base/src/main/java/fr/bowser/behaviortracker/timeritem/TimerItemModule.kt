package fr.bowser.behaviortracker.timeritem

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.time.TimeProvider
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerItemModule(private val screen: TimerItemContract.Screen) {

    @GenericScope(component = TimerItemComponent::class)
    @Provides
    fun provideTimerItemPresenter(
        timeManager: TimeManager,
        timerListManager: TimerListManager,
        settingManager: SettingManager,
        pomodoroManager: PomodoroManager,
        timeProvider: TimeProvider
    ): TimerItemContract.Presenter {
        return TimerItemPresenter(
            screen,
            timeManager,
            timerListManager,
            settingManager,
            pomodoroManager,
            timeProvider
        )
    }
}
