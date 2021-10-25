package fr.bowser.behaviortracker.choosepomodorotimer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ChoosePomodoroTimerModule(
    private val screen: ChoosePomodoroTimerContract.Screen
) {

    @GenericScope(component = ChoosePomodoroTimerComponent::class)
    @Provides
    fun provideChoosePomodoroTimerPresenter(
        timerListManager: TimerListManager,
        pomodoroManager: PomodoroManager
    ): ChoosePomodoroTimerContract.Presenter {
        return ChoosePomodoroTimerPresenter(
            screen,
            timerListManager,
            pomodoroManager
        )
    }
}