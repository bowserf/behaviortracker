package fr.bowser.behaviortracker.create_timer_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class CreateTimerViewModule(private val screen: CreateTimerViewContract.Screen) {

    @GenericScope(component = CreateTimerViewComponent::class)
    @Provides
    fun provideCreateTimerPresenter(
        timeManager: TimerManager,
        timerListManager: TimerListManager,
        pomodoroManager: PomodoroManager,
        eventManager: EventManager,
        timeProvider: TimeProvider
    ): CreateTimerViewContract.Presenter {
        return CreateTimerViewPresenter(
            screen,
            timerListManager,
            pomodoroManager,
            eventManager,
            timeProvider,
            timeManager
        )
    }
}
