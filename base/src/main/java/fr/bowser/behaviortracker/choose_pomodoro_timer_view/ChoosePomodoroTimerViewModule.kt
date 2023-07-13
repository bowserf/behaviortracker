package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ChoosePomodoroTimerViewModule(
    private val screen: ChoosePomodoroTimerViewContract.Screen
) {

    @GenericScope(component = ChoosePomodoroTimerViewComponent::class)
    @Provides
    fun provideChoosePomodoroTimerPresenter(
        timerRepository: TimerRepository,
        pomodoroManager: PomodoroManager
    ): ChoosePomodoroTimerViewContract.Presenter {
        return ChoosePomodoroTimerViewPresenter(
            screen,
            timerRepository,
            pomodoroManager
        )
    }
}
