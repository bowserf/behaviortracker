package fr.bowser.behaviortracker.timer_item_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.navigation.NavigationManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.scroll_to_timer_manager.ScrollToTimerManager
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerItemViewModule(private val screen: TimerItemViewContract.Screen) {

    @GenericScope(component = TimerItemViewComponent::class)
    @Provides
    fun provideTimerItemPresenter(
        scrollToTimerManager: ScrollToTimerManager,
        timeManager: TimerManager,
        timeProvider: TimeProvider,
        timerRepository: TimerRepository,
    ): TimerItemViewContract.Presenter {
        return TimerItemViewPresenter(
            screen,
            scrollToTimerManager,
            timeManager,
            timeProvider,
            timerRepository,
        )
    }
}
