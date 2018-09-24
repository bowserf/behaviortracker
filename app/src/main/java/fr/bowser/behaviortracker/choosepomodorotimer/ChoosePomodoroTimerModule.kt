package fr.bowser.behaviortracker.choosepomodorotimer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ChoosePomodoroTimerModule {

    @GenericScope(component = ChoosePomodoroTimerComponent::class)
    @Provides
    fun provideChoosePomodoroTimerPresenter(timerListManager: TimerListManager,
                                            pomodoroManager: PomodoroManager): ChoosePomodoroTimerPresenter {
        return ChoosePomodoroTimerPresenter(timerListManager, pomodoroManager)
    }

}