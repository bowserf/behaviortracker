package fr.bowser.behaviortracker.pomodoro

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = PomodoroComponent::class)
@Component(
        modules = [(PomodoroModule::class)],
        dependencies = [(BehaviorTrackerAppComponent::class)])
interface PomodoroComponent {

    fun inject(frag: PomodoroFragment)

    fun providePomodoroPresenter(): PomodoroPresenter

}