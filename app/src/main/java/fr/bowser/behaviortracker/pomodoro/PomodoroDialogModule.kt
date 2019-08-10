package fr.bowser.behaviortracker.pomodoro

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class PomodoroDialogModule {

    @GenericScope(component = PomodoroDialogComponent::class)
    @Provides
    fun providePomodoroDialogPresenter(pomodoroManager: PomodoroManager): PomodoroDialogPresenter {
        return PomodoroDialogPresenter(pomodoroManager)
    }
}
