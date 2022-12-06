package fr.bowser.behaviortracker.pomodoro_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = PomodoroViewComponent::class)
@Component(
    modules = [(PomodoroViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface PomodoroViewComponent {

    fun inject(fragment: PomodoroViewFragment)

    fun providePomodoroPresenter(): PomodoroViewContract.Presenter
}
