package fr.bowser.behaviortracker.floating_running_timer

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = FloatingRunningTimerComponent::class)
@Component(
    modules = [(FloatingRunningTimerModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface FloatingRunningTimerComponent {

    fun inject(view: FloatingRunningTimerView)

    fun provideTimerItemPresenter(): FloatingRunningTimerContract.Presenter
}
