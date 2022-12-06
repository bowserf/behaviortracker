package fr.bowser.behaviortracker.home_activity

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = HomeActivityComponent::class)
@Component(
    modules = [(HomeActivityModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface HomeActivityComponent {

    fun inject(activity: HomeActivity)

    fun provideHomePresenter(): HomeActivityContract.Presenter
}
