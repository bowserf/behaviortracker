package fr.bowser.behaviortracker.rewards

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = RewardsComponent::class)
@Component(
        modules = [(RewardsPresenterModule::class)],
        dependencies = [(BehaviorTrackerAppComponent::class)])
interface RewardsComponent {

    fun inject(activity: RewardsActivity)

    fun provideRewardsPresenter(): RewardsPresenter

}