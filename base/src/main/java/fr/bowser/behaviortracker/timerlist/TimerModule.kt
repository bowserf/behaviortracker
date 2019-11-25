package fr.bowser.behaviortracker.timerlist

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerModule(private val timerScreen: TimerContract.Screen) {

    @GenericScope(component = TimerComponent::class)
    @Provides
    fun provideTimerPresenter(
        timerListManager: TimerListManager,
        timeManager: TimeManager
    ): TimerPresenter {
        return TimerPresenter(
            timerScreen,
            timerListManager,
            timeManager
        )
    }
}
