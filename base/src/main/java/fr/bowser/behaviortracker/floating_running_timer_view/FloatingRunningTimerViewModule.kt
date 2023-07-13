package fr.bowser.behaviortracker.floating_running_timer_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class FloatingRunningTimerViewModule(private val screen: FloatingRunningTimerViewContract.Screen) {

    @GenericScope(component = FloatingRunningTimerViewComponent::class)
    @Provides
    fun provideTimerItemPresenter(
        timeManager: TimerManager,
        timerRepository: TimerRepository
    ): FloatingRunningTimerViewContract.Presenter {
        return FloatingRunningTimerViewPresenter(
            screen,
            timeManager,
            timerRepository
        )
    }
}
