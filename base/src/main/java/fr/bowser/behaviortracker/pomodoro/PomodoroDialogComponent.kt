package fr.bowser.behaviortracker.pomodoro

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = PomodoroDialogComponent::class)
@Component(
    modules = [(PomodoroDialogModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface PomodoroDialogComponent {

    fun inject(fragment: PomodoroDialog)

    fun providePomodoroDialogPresenter(): PomodoroDialogContract.Presenter
}
