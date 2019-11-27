package fr.bowser.behaviortracker.pomodoro

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class PomodoroModule(private val pomodoroScreen: PomodoroContract.Screen) {

    @GenericScope(component = PomodoroComponent::class)
    @Provides
    fun providePomodoroPresenter(
        pomodoroManager: PomodoroManager,
        timerListManager: TimerListManager,
        instantAppManager: InstantAppManager
    ): PomodoroPresenter {
        return PomodoroPresenter(
                pomodoroScreen,
                pomodoroManager,
                timerListManager.getTimerList(),
                instantAppManager.isInstantApp()
        )
    }
}
