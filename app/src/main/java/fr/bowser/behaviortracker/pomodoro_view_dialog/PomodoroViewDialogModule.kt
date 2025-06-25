package fr.bowser.behaviortracker.pomodoro_view_dialog

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class PomodoroViewDialogModule {

    @GenericScope(component = PomodoroViewDialogComponent::class)
    @Provides
    fun providePomodoroDialogPresenter(
        pomodoroManager: PomodoroManager,
    ): PomodoroViewDialogContract.Presenter {
        return PomodoroViewDialogPresenter(pomodoroManager)
    }
}
