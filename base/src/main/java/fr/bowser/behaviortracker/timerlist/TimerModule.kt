package fr.bowser.behaviortracker.timerlist

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature_string.StringManager

@Module
class TimerModule(private val timerScreen: TimerContract.Screen) {

    @GenericScope(component = TimerComponent::class)
    @Provides
    fun provideTimerPresenter(
        timerListManager: TimerListManager,
        timeManager: TimeManager,
        stringManager: StringManager,
        instantAppManager: InstantAppManager
    ): TimerContract.Presenter {
        return TimerPresenter(
            timerScreen,
            timerListManager,
            timeManager,
            stringManager,
            instantAppManager.isInstantApp()
        )
    }
}
