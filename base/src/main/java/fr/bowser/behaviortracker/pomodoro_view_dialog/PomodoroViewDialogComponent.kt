package fr.bowser.behaviortracker.pomodoro_view_dialog

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = PomodoroViewDialogComponent::class)
@Component(
    modules = [(PomodoroViewDialogModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface PomodoroViewDialogComponent {

    fun inject(fragment: PomodoroViewDialog)

    fun providePomodoroDialogPresenter(): PomodoroViewDialogContract.Presenter
}
