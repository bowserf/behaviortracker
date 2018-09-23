package fr.bowser.behaviortracker.choosepomodorotimer

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.PauseTimer
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ChoosePomodoroTimerModule {

    @GenericScope(component = ChoosePomodoroTimerComponent::class)
    @Provides
    fun provideChoosePomodoroTimerPresenter(context: Context,
                                            timerListManager: TimerListManager,
                                            pomodoroManager: PomodoroManager): ChoosePomodoroTimerPresenter {
        val pauseTimer = PauseTimer.getTimer(context)
        return ChoosePomodoroTimerPresenter(timerListManager, pomodoroManager, pauseTimer)
    }

}