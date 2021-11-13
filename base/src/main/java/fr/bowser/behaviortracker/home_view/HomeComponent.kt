package fr.bowser.behaviortracker.home_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = HomeComponent::class)
@Component(
    modules = [(HomeModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface HomeComponent {

    fun inject(activity: HomeActivity)

    fun provideHomePresenter(): HomeContract.Presenter
}
