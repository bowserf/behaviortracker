package fr.bowser.behaviortracker.createtimer

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = CreateTimerComponent::class)
@Component(
        modules = arrayOf(CreateTimerModule::class),
        dependencies = arrayOf(BehaviorTrackerAppComponent::class))
interface CreateTimerComponent {

    fun inject(activity: CreateTimerActivity)

    fun provideCreateTimerPresenter(): CreateTimerPresenter

}