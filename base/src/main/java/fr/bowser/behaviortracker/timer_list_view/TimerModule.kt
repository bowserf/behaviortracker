package fr.bowser.behaviortracker.timer_list_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature_string.StringManager

@Module
class TimerModule(private val timerScreen: TimerContract.Screen) {

    @GenericScope(component = TimerComponent::class)
    @Provides
    fun provideTimerPresenter(
        alarmTimerManager: AlarmTimerManager,
        instantAppManager: InstantAppManager,
        timerListManager: TimerListManager,
        timeManager: TimeManager,
        stringManager: StringManager
    ): TimerContract.Presenter {
        return TimerPresenter(
            timerScreen,
            alarmTimerManager,
            timerListManager,
            timeManager,
            stringManager,
            instantAppManager.isInstantApp()
        )
    }
}
