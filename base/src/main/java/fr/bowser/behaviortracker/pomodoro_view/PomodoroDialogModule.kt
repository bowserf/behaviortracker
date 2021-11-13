package fr.bowser.behaviortracker.pomodoro_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class PomodoroDialogModule {

    @GenericScope(component = PomodoroDialogComponent::class)
    @Provides
    fun providePomodoroDialogPresenter(
        pomodoroManager: PomodoroManager
    ): PomodoroDialogContract.Presenter {
        return PomodoroDialogPresenter(pomodoroManager)
    }
}
