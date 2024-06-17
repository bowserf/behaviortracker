package fr.bowser.behaviortracker.floating_running_timer_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = FloatingRunningTimerViewComponent::class)
@Component(
    modules = [(FloatingRunningTimerViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)],
)
interface FloatingRunningTimerViewComponent {

    fun inject(view: FloatingRunningTimerView)

    fun provideTimerItemPresenter(): FloatingRunningTimerViewContract.Presenter
}
