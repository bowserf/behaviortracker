package fr.bowser.behaviortracker.createtimer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class CreateTimerModule(private val screen: CreateTimerContract.Screen) {

    @GenericScope(component = CreateTimerComponent::class)
    @Provides
    fun provideCreateTimerPresenter(
        timerListManager: TimerListManager,
        timeManager: TimeManager,
        pomodoroManager: PomodoroManager,
        eventManager: EventManager
    ): CreateTimerContract.Presenter {
        return CreateTimerPresenter(
            screen,
            timerListManager,
            timeManager,
            pomodoroManager,
            eventManager
        )
    }
}