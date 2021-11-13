package fr.bowser.behaviortracker.showmode

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ShowModeComponent::class)
@Component(
    modules = arrayOf(ShowModeModule::class),
    dependencies = arrayOf(BehaviorTrackerAppComponent::class)
)
interface ShowModeComponent {

    fun inject(view: ShowModeFragment)

    fun provideShowModePresenter(): ShowModeContract.Presenter
}
