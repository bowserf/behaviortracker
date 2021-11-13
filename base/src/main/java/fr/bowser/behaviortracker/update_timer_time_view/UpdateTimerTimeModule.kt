package fr.bowser.behaviortracker.update_timer_time_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class UpdateTimerTimeModule(
    private val screen: UpdateTimerTimeContract.Screen,
    private val timerId: Long
) {

    @GenericScope(component = UpdateTimerTimeComponent::class)
    @Provides
    fun provideUpdateTimerTimePresenter(
        timerListManager: TimerListManager,
        timeManager: TimeManager
    ): UpdateTimerTimeContract.Presenter {
        return UpdateTimerTimePresenter(
            screen,
            timerListManager,
            timeManager,
            timerId
        )
    }
}
