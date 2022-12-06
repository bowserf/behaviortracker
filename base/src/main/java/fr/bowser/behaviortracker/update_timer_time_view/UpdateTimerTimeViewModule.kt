package fr.bowser.behaviortracker.update_timer_time_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class UpdateTimerTimeViewModule(
    private val screen: UpdateTimerTimeViewContract.Screen,
    private val timerId: Long
) {

    @GenericScope(component = UpdateTimerTimeViewComponent::class)
    @Provides
    fun provideUpdateTimerTimePresenter(
        timerListManager: TimerListManager,
        timeManager: TimerManager
    ): UpdateTimerTimeViewContract.Presenter {
        return UpdateTimerTimeViewPresenter(
            screen,
            timerListManager,
            timeManager,
            timerId
        )
    }
}
