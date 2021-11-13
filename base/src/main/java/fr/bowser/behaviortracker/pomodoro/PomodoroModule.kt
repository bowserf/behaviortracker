package fr.bowser.behaviortracker.pomodoro

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager

@Module
class PomodoroModule(private val pomodoroScreen: PomodoroContract.Screen) {

    @GenericScope(component = PomodoroComponent::class)
    @Provides
    fun providePomodoroPresenter(
        pomodoroManager: PomodoroManager,
        timerListManager: TimerListManager,
        instantAppManager: InstantAppManager,
        doNotDisturbManager: DoNotDisturbManager
    ): PomodoroContract.Presenter {
        return PomodoroPresenter(
            pomodoroScreen,
            pomodoroManager,
            doNotDisturbManager,
            timerListManager,
            instantAppManager.isInstantApp()
        )
    }
}
