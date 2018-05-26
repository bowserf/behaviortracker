package fr.bowser.behaviortracker.createtimer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class CreateTimerModule(private val view: CreateTimerContract.View) {

    @GenericScope(component = CreateTimerComponent::class)
    @Provides
    fun provideCreateTimerPresenter(timerListManager: TimerListManager,
                                    timeManager: TimeManager,
                                    eventManager: EventManager): CreateTimerPresenter {
        return CreateTimerPresenter(view, timerListManager, timeManager, eventManager)
    }

}