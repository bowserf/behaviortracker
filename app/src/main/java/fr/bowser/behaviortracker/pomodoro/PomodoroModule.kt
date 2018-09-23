package fr.bowser.behaviortracker.pomodoro

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class PomodoroModule(private val pomodoroScreen: PomodoroContract.Screen) {

    @GenericScope(component = PomodoroComponent::class)
    @Provides
    fun providePomodoroPresenter(): PomodoroPresenter {
        return PomodoroPresenter()
    }

}
