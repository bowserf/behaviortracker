package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ChoosePomodoroTimerViewModule(
    private val screen: ChoosePomodoroTimerViewContract.Screen
) {

    @GenericScope(component = ChoosePomodoroTimerViewComponent::class)
    @Provides
    fun provideChoosePomodoroTimerPresenter(
        timerListManager: TimerListManager,
        pomodoroManager: PomodoroManager
    ): ChoosePomodoroTimerViewContract.Presenter {
        return ChoosePomodoroTimerViewPresenter(
            screen,
            timerListManager,
            pomodoroManager
        )
    }
}
