package fr.bowser.behaviortracker.timer_service

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = TimerServiceComponent::class)
@Component(
    modules = [(TimerServiceModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface TimerServiceComponent {

    fun inject(service: TimerService)

    fun provideTimePresenter(): TimerServiceContract.Presenter
}
