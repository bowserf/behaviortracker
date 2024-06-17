package fr.bowser.behaviortracker.rewards_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = RewardsViewComponent::class)
@Component(
    modules = [(RewardsViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)],
)
interface RewardsViewComponent {

    fun inject(activity: RewardsViewFragment)

    fun provideRewardsPresenter(): RewardsViewContract.Presenter
}
