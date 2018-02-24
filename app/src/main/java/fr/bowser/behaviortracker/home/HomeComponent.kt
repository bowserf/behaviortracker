package fr.bowser.behaviortracker.home


import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = HomeComponent::class)
@Component(
        modules = arrayOf(HomeModule::class),
        dependencies = arrayOf(BehaviorTrackerAppComponent::class))
interface HomeComponent {

    fun inject(activity: HomeActivity)

    fun provideHomePresenter(): HomePresenter

}