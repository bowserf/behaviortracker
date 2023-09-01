package fr.bowser.behaviortracker.pomodoro_choose_timer_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class PomodoroChooseTimerViewModule(
    private val screen: PomodoroChooseTimerViewContract.Screen
) {

    @GenericScope(component = PomodoroChooseTimerViewComponent::class)
    @Provides
    fun providePomodoroChooseTimerPresenter(
        timerRepository: TimerRepository,
        pomodoroManager: PomodoroManager
    ): PomodoroChooseTimerViewContract.Presenter {
        return PomodoroChooseTimerViewPresenter(
            screen,
            timerRepository,
            pomodoroManager
        )
    }
}
