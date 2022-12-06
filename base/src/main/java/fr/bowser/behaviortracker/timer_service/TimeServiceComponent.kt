package fr.bowser.behaviortracker.timer_service

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = TimeServiceComponent::class)
@Component(
    modules = [(TimeServiceModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface TimeServiceComponent {

    fun inject(service: TimeService)

    fun provideTimePresenter(): TimeServiceContract.Presenter
}
