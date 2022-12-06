package fr.bowser.behaviortracker.pomodoro_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager

@Module
class PomodoroViewModule(private val pomodoroScreen: PomodoroViewContract.Screen) {

    @GenericScope(component = PomodoroViewComponent::class)
    @Provides
    fun providePomodoroPresenter(
        pomodoroManager: PomodoroManager,
        timerListManager: TimerListManager,
        instantAppManager: InstantAppManager,
        doNotDisturbManager: DoNotDisturbManager
    ): PomodoroViewContract.Presenter {
        return PomodoroViewPresenter(
            pomodoroScreen,
            pomodoroManager,
            doNotDisturbManager,
            timerListManager,
            instantAppManager.isInstantApp()
        )
    }
}
