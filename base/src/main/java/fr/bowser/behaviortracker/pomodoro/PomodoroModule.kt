package fr.bowser.behaviortracker.pomodoro

import dagger.Module
import dagger.Provides
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager
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
        instantAppManager: InstantAppManager,
        doNotDisturbManager: fr.bowser.feature_do_not_disturb.DoNotDisturbManager
    ): PomodoroPresenter {
        return PomodoroPresenter(
            pomodoroScreen,
            pomodoroManager,
            doNotDisturbManager,
            timerListManager.getTimerList(),
            instantAppManager.isInstantApp()
        )
    }
}
